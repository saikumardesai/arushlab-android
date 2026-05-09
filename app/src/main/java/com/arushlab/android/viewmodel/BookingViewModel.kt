package com.arushlab.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arushlab.android.model.BookingModel
import com.arushlab.android.model.TestModel
import com.arushlab.android.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    private val _bookingState = MutableStateFlow<UiState<BookingModel>?>(null)
    val bookingState: StateFlow<UiState<BookingModel>?> = _bookingState.asStateFlow()

    fun submitBooking(patientName: String, mobile: String, address: String, test: TestModel) {
        viewModelScope.launch {
            _bookingState.value = UiState.Loading

            // Simple validation
            if (patientName.length < 3) {
                _bookingState.value = UiState.Error("Patient name must be at least 3 characters")
                return@launch
            }
            if (mobile.length != 10 || !mobile.all { it.isDigit() }) {
                _bookingState.value = UiState.Error("Enter a valid 10-digit mobile number")
                return@launch
            }
            if (address.isBlank()) {
                _bookingState.value = UiState.Error("Address cannot be empty")
                return@launch
            }

            val trackingId = "ARUSH-${(1000..9999).random()}"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            
            val booking = BookingModel(
                id = trackingId,
                patientName = patientName,
                mobileNumber = mobile,
                address = address,
                testName = test.name,
                status = "Booking Confirmed",
                date = dateFormat.format(Date())
            )

            val result = repository.createBooking(booking)
            result.fold(
                onSuccess = { savedBooking ->
                    _bookingState.value = UiState.Success(savedBooking)
                },
                onFailure = { error ->
                    _bookingState.value = UiState.Error(error.localizedMessage ?: "Failed to submit booking")
                }
            )
        }
    }

    fun resetBookingState() {
        _bookingState.value = null
    }
}
