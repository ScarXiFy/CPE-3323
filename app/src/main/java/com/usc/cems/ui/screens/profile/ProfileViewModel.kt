package com.usc.cems.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.usc.cems.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var isAdmin by mutableStateOf(false)
        private set

    var userEmail by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        authRepository.getCurrentUser()?.let { user ->
            isAdmin = user.role == "Admin"
            userEmail = user.email
            userName = user.fullname
        }
    }

    fun logout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }
}
