package com.arushlab.android.repository

import android.util.Log
import com.arushlab.android.model.TestModel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

import com.arushlab.android.local.TestDao
import com.arushlab.android.local.toEntity
import com.arushlab.android.local.toModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.emitAll

interface TestRepository {
    fun getTests(): Flow<Result<List<TestModel>>>
}

class TestRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val testDao: TestDao
) : TestRepository {

    override fun getTests(): Flow<Result<List<TestModel>>> = flow {
        // First, emit whatever is in the local cache immediately
        val cachedTests = try {
            testDao.getAllTestsOnce()
        } catch (e: Exception) {
            Log.w("TestRepository", "Cache read failed: ${e.message}")
            emptyList()
        }

        if (cachedTests.isNotEmpty()) {
            Log.d("TestRepository", "Emitting ${cachedTests.size} cached tests")
            emit(Result.success(cachedTests.map { it.toModel() }))
        }

        // Then fetch from Supabase — only select columns that exist in the DB
        try {
            Log.d("TestRepository", "Fetching tests from Supabase...")
            val response = postgrest["tests"]
                .select(Columns.list("id", "name", "price", "category", "is_active", "created_at")) {
                    order("name", io.github.jan.supabase.postgrest.query.Order.ASCENDING)
                }
                .decodeList<TestModel>()
            Log.d("TestRepository", "Fetched ${response.size} tests from Supabase")

            // Filter to only active tests
            val activeTests = response.filter { it.isActive }

            if (activeTests.isNotEmpty()) {
                // Save to local DB
                try {
                    testDao.clearAll()
                    testDao.insertAll(activeTests.map { it.toEntity() })
                } catch (e: Exception) {
                    Log.w("TestRepository", "Cache write failed: ${e.message}")
                }
                emit(Result.success(activeTests))
            } else if (cachedTests.isEmpty()) {
                emit(Result.success(emptyList()))
            }
        } catch (e: Exception) {
            Log.e("TestRepository", "Supabase fetch failed: ${e.message}", e)
            // If we already emitted cached data, don't emit error
            if (cachedTests.isEmpty()) {
                emit(Result.failure(e))
            }
        }

        // Also observe Room for future updates
        emitAll(
            testDao.getAllTests()
                .map { entities -> Result.success(entities.map { it.toModel() }) }
                .catch { emit(Result.failure(it)) }
        )
    }
}
