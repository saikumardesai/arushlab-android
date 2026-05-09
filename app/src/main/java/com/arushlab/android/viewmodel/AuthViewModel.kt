package com.arushlab.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
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

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                _authState.value = UiState.Success(true)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.localizedMessage ?: "Failed to sign in")
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                supabaseClient.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                _authState.value = UiState.Success(true)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.localizedMessage ?: "Failed to sign up")
            }
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
