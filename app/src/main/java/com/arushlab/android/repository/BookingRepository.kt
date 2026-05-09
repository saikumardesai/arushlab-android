package com.arushlab.android.repository

import com.arushlab.android.model.BookingModel
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BookingRepository {
    fun getBookingByTrackingId(trackingId: String): Flow<Result<BookingModel>>
    suspend fun createBooking(booking: BookingModel): Result<BookingModel>
}

class BookingRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : BookingRepository {
    override fun getBookingByTrackingId(trackingId: String): Flow<Result<BookingModel>> = flow {
        try {
            val response = postgrest["bookings"]
                .select {
                    filter {
                        eq("id", trackingId)
                    }
                }
                .decodeSingle<BookingModel>()
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun createBooking(booking: BookingModel): Result<BookingModel> {
        return try {
            val response = postgrest["bookings"]
                .insert(booking)
                .decodeSingle<BookingModel>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
