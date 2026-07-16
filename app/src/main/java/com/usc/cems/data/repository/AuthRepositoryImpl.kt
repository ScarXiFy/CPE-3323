package com.usc.cems.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.usc.cems.data.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private var cachedUser: UserProfile? = null

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val trimmedEmail = email.trim()
        if (trimmedEmail == "21700003@usc.edu.ph" && password != "admin123") {
            throw Exception("Invalid admin credentials")
        }
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(trimmedEmail, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Authentication failed: Empty user returned.")

            // Retrieve user profile document from Cloud Firestore
            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
            val role = if (trimmedEmail == "21700003@usc.edu.ph") {
                "admin"
            } else {
                val r = userDoc.getString("role") ?: "student"
                if (r == "user") "student" else r
            }
            val fullname = userDoc.getString("fullname") ?: firebaseUser.displayName ?: if (trimmedEmail == "21700003@usc.edu.ph") "USC Admin" else "USC Student"

            cachedUser = UserProfile(
                uid = firebaseUser.uid,
                fullname = fullname,
                email = trimmedEmail,
                role = role
            )
        } catch (e: Exception) {
            // Resilient seeding: Auto-register the admin credentials if login fails due to user not found
            if (trimmedEmail == "21700003@usc.edu.ph" && password == "admin123") {
                register(trimmedEmail, "USC Admin", password).getOrThrow()
                // Retry sign-in
                val authResult = firebaseAuth.signInWithEmailAndPassword(trimmedEmail, password).await()
                val firebaseUser = authResult.user ?: throw Exception("Auth failed after admin seeding.")
                cachedUser = UserProfile(
                    uid = firebaseUser.uid,
                    fullname = "USC Admin",
                    email = trimmedEmail,
                    role = "admin"
                )
            } else {
                throw e
            }
        }
    }

    override suspend fun register(email: String, fullname: String, password: String): Result<Unit> = runCatching {
        val trimmedEmail = email.trim()
        if (trimmedEmail == "21700003@usc.edu.ph" && password != "admin123") {
            throw Exception("Registration failed: Admin email must use the designated admin password.")
        }
        val authResult = firebaseAuth.createUserWithEmailAndPassword(trimmedEmail, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Registration failed: Empty user returned.")

        // Update display name in Firebase Auth
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullname.trim())
            .build()
        firebaseUser.updateProfile(profileUpdates).await()

        // Set default role: admin for the seeded admin email, student for others
        val role = if (trimmedEmail == "21700003@usc.edu.ph") "admin" else "student"
        val userMap = hashMapOf(
            "uid" to firebaseUser.uid,
            "fullname" to fullname.trim(),
            "email" to trimmedEmail,
            "role" to role
        )

        // Save profile configuration directly to users Firestore collection
        firestore.collection("users").document(firebaseUser.uid).set(userMap).await()

        cachedUser = UserProfile(
            uid = firebaseUser.uid,
            fullname = fullname.trim(),
            email = trimmedEmail,
            role = role
        )
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): UserProfile? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        if (cachedUser == null || cachedUser?.uid != firebaseUser.uid) {
            // Return cached user or fallback to auth info until firestore is loaded
            val email = firebaseUser.email ?: ""
            val role = if (email == "21700003@usc.edu.ph") "admin" else "student"
            cachedUser = UserProfile(
                uid = firebaseUser.uid,
                fullname = firebaseUser.displayName ?: "USC Student",
                email = email,
                role = role
            )
        }
        return cachedUser
    }

    override fun logout() {
        cachedUser = null
        firebaseAuth.signOut()
    }
}
