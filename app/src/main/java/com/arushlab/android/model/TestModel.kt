package com.arushlab.android.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestModel(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("price") val price: Double,
    @SerialName("category") val category: String,
    @SerialName("description") val description: String? = null,
    @SerialName("image_url") val imageUrl: String? = null
)
