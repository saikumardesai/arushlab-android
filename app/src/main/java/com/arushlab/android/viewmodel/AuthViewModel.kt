package com.arushlab.android.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<Boolean>?>(null)
    val authState: StateFlow<UiState<Boolean>?> = _authState.asStateFlow()

    // Check if user is currently logged in
    val sessionStatus = supabaseClient.auth.sessionStatus

    val currentUserEmail: String
        get() = try { supabaseClient.auth.currentUserOrNull()?.email ?: "member@arushlab.com" } catch (e: Exception) { "member@arushlab.com" }

    // Debounce protection: prevent rapid repeated calls
    private var authJob: Job? = null
    private var lastAuthAttemptTime = 0L
    private val AUTH_DEBOUNCE_MS = 3000L // 3 seconds minimum between attempts

    fun signIn(userEmail: String, userPassword: String) {
        if (!canAttemptAuth()) return

        authJob?.cancel()
        authJob = viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                Log.d("AuthViewModel", "Attempting sign in for: $userEmail")
                supabaseClient.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                
                // Confirm session is active
                delay(500)
                val currentSession = try { supabaseClient.auth.currentSessionOrNull() } catch (e: Exception) { null }
                
                if (currentSession != null) {
                    Log.d("AuthViewModel", "Sign in successful — session active")
                    _authState.value = UiState.Success(true)
                } else {
                    Log.w("AuthViewModel", "Sign in call returned but no session active")
                    _authState.value = UiState.Error("Login successful, but session could not be established. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign in failed: ${e.message}", e)
                val errorMsg = parseAuthError(e)
                _authState.value = UiState.Error(errorMsg)
            }
        }
    }

    fun signUp(userEmail: String, userPassword: String) {
        if (!canAttemptAuth()) return

        authJob?.cancel()
        authJob = viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                Log.d("AuthViewModel", "Attempting sign up for: $userEmail")
                supabaseClient.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }

                // Check if auto-confirm is enabled by checking session status
                // If session was created directly, user is auto-confirmed
                delay(500) // Brief wait for session to propagate
                val currentSession = try {
                    supabaseClient.auth.currentSessionOrNull()
                } catch (e: Exception) {
                    null
                }

                if (currentSession != null) {
                    // Auto-confirm is enabled, user is logged in
                    Log.d("AuthViewModel", "Sign up successful — session active (auto-confirm enabled)")
                    _authState.value = UiState.Success(true)
                } else {
                    // Email confirmation is required — do NOT attempt sign-in (avoids rate limit)
                    Log.d("AuthViewModel", "Sign up successful — email confirmation may be required")
                    _authState.value = UiState.Error(
                        "Account created! Please check your email to confirm, then log in. " +
                        "If you don't receive an email, disable email confirmation in Supabase Dashboard → Authentication → Settings."
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign up failed: ${e.message}", e)
                val errorMsg = parseAuthError(e)
                _authState.value = UiState.Error(errorMsg)
            }
        }
    }

    /**
     * Debounce check to prevent rapid auth calls that trigger rate limiting.
     * Returns false (and sets error state) if called too quickly after the last attempt.
     */
    private fun canAttemptAuth(): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastAuthAttemptTime < AUTH_DEBOUNCE_MS && _authState.value is UiState.Loading) {
            // Already in progress, ignore
            return false
        }
        if (now - lastAuthAttemptTime < AUTH_DEBOUNCE_MS && _authState.value is UiState.Error) {
            val lastError = (_authState.value as UiState.Error).message
            if (lastError.contains("Too many attempts", ignoreCase = true)) {
                // Still in rate limit cooldown
                return false
            }
        }
        lastAuthAttemptTime = now
        return true
    }

    private fun parseAuthError(e: Exception): String {
        val msg = e.message ?: e.localizedMessage ?: "Authentication failed"
        return when {
            msg.contains("rate limit", ignoreCase = true) ||
            msg.contains("429", ignoreCase = true) ||
            msg.contains("too many requests", ignoreCase = true) ||
            msg.contains("over_request_rate_limit", ignoreCase = true) ||
            msg.contains("over_email_send_rate_limit", ignoreCase = true) ->
                "Too many attempts. Supabase rate limit reached.\n\nPlease wait 60 seconds and try again.\n\nTip: In Supabase Dashboard → Authentication → Rate Limits, increase the limits if you're testing."
            msg.contains("email not confirmed", ignoreCase = true) ->
                "Please confirm your email first.\n\nTip: To disable email confirmation for testing, go to Supabase Dashboard → Authentication → Providers → Email → Disable 'Confirm email'."
            msg.contains("invalid login", ignoreCase = true) ||
            msg.contains("invalid credentials", ignoreCase = true) ->
                "Invalid email or password. Please check and try again."
            msg.contains("user already registered", ignoreCase = true) ->
                "This email is already registered. Try logging in instead."
            msg.contains("signup_disabled", ignoreCase = true) ->
                "Sign-ups are currently disabled. Enable them in Supabase Dashboard → Authentication → Settings."
            msg.contains("network", ignoreCase = true) ||
            msg.contains("connect", ignoreCase = true) ||
            msg.contains("unable to resolve host", ignoreCase = true) ||
            msg.contains("timeout", ignoreCase = true) ->
                "Network error. Please check your internet connection."
            msg.contains("password", ignoreCase = true) && msg.contains("short", ignoreCase = true) ->
                "Password must be at least 6 characters."
            else -> msg
        }
    }
    
    fun resetState() {
        _authState.value = null
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabaseClient.auth.signOut()
            } catch (e: Exception) {
                // Ignore sign out errors
            }
        }
    }
}
