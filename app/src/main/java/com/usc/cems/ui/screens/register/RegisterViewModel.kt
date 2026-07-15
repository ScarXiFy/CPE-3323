package com.usc.cems.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc.cems.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var fullname by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var fullnameError by mutableStateOf<String?>(null)
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    private val _registerSuccess = MutableSharedFlow<Unit>()
    val registerSuccess: SharedFlow<Unit> = _registerSuccess.asSharedFlow()

    fun onFullnameChange(newValue: String) {
        fullname = newValue
        fullnameError = null
        authError = null
    }

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = null
        authError = null
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordError = null
        authError = null
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword = newValue
        confirmPasswordError = null
        authError = null
    }

    fun register() {
        if (!validateInputs()) return

        isLoading = true
        authError = null

        viewModelScope.launch {
            authRepository.register(email.trim(), fullname.trim(), password)
                .onSuccess {
                    _registerSuccess.emit(Unit)
                }
                .onFailure { exception ->
                    authError = exception.localizedMessage ?: "Registration failed. Please try again."
                }
            isLoading = false
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (fullname.isBlank()) {
            fullnameError = "Full Name is required"
            isValid = false
        }

        if (email.isBlank()) {
            emailError = "Email Address is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailError = "Please enter a valid email address"
            isValid = false
        }

        if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }

        if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }

        return isValid
    }
}
