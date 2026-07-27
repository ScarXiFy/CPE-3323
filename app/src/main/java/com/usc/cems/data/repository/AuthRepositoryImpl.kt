package com.usc.cems.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.usc.cems.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val authResult = firebaseAuth.signInWithEmailAndPassword(trimmedEmail, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Authentication failed: Empty user returned.")

        // Retrieve user profile document from Cloud Firestore
        val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
        val rawRole = userDoc.getString("role") ?: "student"
        val role = if (rawRole == "user") "student" else rawRole
        val fullname = userDoc.getString("fullname") ?: firebaseUser.displayName ?: "USC Student"

        cachedUser = UserProfile(
            uid = firebaseUser.uid,
            fullname = fullname,
            email = trimmedEmail,
            role = role
        )
    }

    override suspend fun register(email: String, fullname: String, password: String): Result<Unit> = runCatching {
        val trimmedEmail = email.trim()
        val authResult = firebaseAuth.createUserWithEmailAndPassword(trimmedEmail, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Registration failed: Empty user returned.")

        // Update display name in Firebase Auth
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullname.trim())
            .build()
        firebaseUser.updateProfile(profileUpdates).await()

        val role = "student"
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
            val email = firebaseUser.email ?: ""
            cachedUser = UserProfile(
                uid = firebaseUser.uid,
                fullname = firebaseUser.displayName ?: "USC Student",
                email = email,
                role = "student"
            )
            fetchUserProfileFromFirestore(firebaseUser.uid)
        }
        return cachedUser
    }

    private fun fetchUserProfileFromFirestore(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userDoc = firestore.collection("users").document(uid).get().await()
                if (userDoc.exists() && firebaseAuth.currentUser?.uid == uid) {
                    val rawRole = userDoc.getString("role") ?: "student"
                    val role = if (rawRole == "user") "student" else rawRole
                    val fullname = userDoc.getString("fullname") ?: firebaseAuth.currentUser?.displayName ?: "USC Student"
                    val email = userDoc.getString("email") ?: firebaseAuth.currentUser?.email ?: ""
                    cachedUser = UserProfile(
                        uid = uid,
                        fullname = fullname,
                        email = email,
                        role = role
                    )
                }
            } catch (_: Exception) {}
        }
    }

    override fun logout() {
        cachedUser = null
        firebaseAuth.signOut()
    }
}
