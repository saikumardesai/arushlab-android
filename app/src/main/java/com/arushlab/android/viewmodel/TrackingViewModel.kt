package com.arushlab.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arushlab.android.model.BookingModel
import com.arushlab.android.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<BookingModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<BookingModel>> = _uiState.asStateFlow()

    fun fetchBooking(trackingId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getBookingByTrackingId(trackingId).collect { result ->
                result.fold(
                    onSuccess = { booking ->
                        _uiState.value = UiState.Success(booking)
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.localizedMessage ?: "An unexpected error occurred")
                    }
                )
            }
        }
    }
}
