package com.arushlab.android.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arushlab.android.model.TestModel

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val description: String?,
    val imageUrl: String?
)

fun TestEntity.toModel(): TestModel {
    return TestModel(
        id = id,
        name = name,
        price = price,
        category = category,
        description = description,
        imageUrl = imageUrl
    )
}

fun TestModel.toEntity(): TestEntity {
    return TestEntity(
        id = id,
        name = name,
        price = price,
        category = category,
        description = description,
        imageUrl = imageUrl
    )
}
