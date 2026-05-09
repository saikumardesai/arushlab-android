package com.arushlab.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingModel(
    @SerialName("id") val id: String,
    @SerialName("patient_name") val patientName: String,
    @SerialName("phone") val phone: String,
    @SerialName("address") val address: String,
    @SerialName("test_name") val testName: String,
    @SerialName("status") val status: String, // e.g., Booking Confirmed, Lab Technician Assigned, Sample Collected, Testing in Laboratory, Report Ready
    @SerialName("report_url") val reportUrl: String? = null,
    @SerialName("report_uploaded_at") val reportUploadedAt: String? = null,
    @SerialName("date") val date: String,
    @SerialName("phlebotomist_name") val phlebotomistName: String? = null
)
