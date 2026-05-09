package com.arushlab.android.repository

import com.arushlab.android.model.TestModel
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import com.arushlab.android.local.TestDao
import com.arushlab.android.local.toEntity
import com.arushlab.android.local.toModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface TestRepository {
    fun getTests(): Flow<Result<List<TestModel>>>
}

class TestRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val testDao: TestDao
) : TestRepository {
    
    override fun getTests(): Flow<Result<List<TestModel>>> {
        // Trigger network fetch in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = postgrest["tests"].select().decodeList<TestModel>()
                testDao.insertAll(response.map { it.toEntity() })
            } catch (e: Exception) {
                // Ignore network errors, rely on cache if available
            }
        }

        // Return flow from Room DB (Single Source of Truth)
        return testDao.getAllTests()
            .map { entities -> Result.success(entities.map { it.toModel() }) }
            .catch { emit(Result.failure(it)) }
    }
}
