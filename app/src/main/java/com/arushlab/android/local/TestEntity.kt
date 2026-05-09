package com.arushlab.android.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arushlab.android.model.TestModel

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val isActive: Boolean
)

fun TestEntity.toModel(): TestModel {
    return TestModel(
        id = id,
        name = name,
        price = price,
        category = category,
        isActive = isActive
    )
}

fun TestModel.toEntity(): TestEntity {
    return TestEntity(
        id = id,
        name = name,
        price = price,
        category = category,
        isActive = isActive
    )
}
