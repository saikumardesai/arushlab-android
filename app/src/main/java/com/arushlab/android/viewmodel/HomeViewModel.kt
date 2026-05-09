package com.arushlab.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arushlab.android.model.TestModel
import com.arushlab.android.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<TestModel>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<TestModel>>> = _uiState.asStateFlow()

    private var allTests = emptyList<TestModel>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchTests()
    }

    fun fetchTests() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getTests().collect { result ->
                result.fold(
                    onSuccess = { tests ->
                        allTests = tests
                        _uiState.value = UiState.Success(tests)
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(error.localizedMessage ?: "Failed to fetch tests")
                    }
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterTests(query)
    }

    private fun filterTests(query: String) {
        if (query.isEmpty()) {
            _uiState.value = UiState.Success(allTests)
        } else {
            val filtered = allTests.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
            _uiState.value = UiState.Success(filtered)
        }
    }
}
