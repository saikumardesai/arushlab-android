package com.arushlab.android.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TestEntity::class], version = 1, exportSchema = false)
abstract class ArushLabDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao
}
