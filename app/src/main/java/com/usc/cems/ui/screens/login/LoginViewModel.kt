package com.usc.cems.ui.screens.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    private val _loginSuccess = MutableSharedFlow<Unit>()
    val loginSuccess: SharedFlow<Unit> = _loginSuccess.asSharedFlow()

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

    fun login() {
        if (!validateInputs()) return

        isLoading = true
        authError = null

        viewModelScope.launch {
            authRepository.login(email.trim(), password)
                .onSuccess {
                    _loginSuccess.emit(Unit)
                }
                .onFailure { exception ->
                    authError = exception.localizedMessage ?: "Invalid email or password"
                }
            isLoading = false
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (email.isBlank()) {
            emailError = "Email Address is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailError = "Please enter a valid email address"
            isValid = false
        }

        if (password.isBlank()) {
            passwordError = "Password is required"
            isValid = false
        }

        return isValid
    }
}
