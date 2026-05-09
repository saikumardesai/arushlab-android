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

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow(listOf("All"))
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

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
                        
                        val uniqueNames = tests.map { it.name }.filter { it.isNotBlank() }.distinct().sorted()
                        _categories.value = listOf("All") + uniqueNames
                        
                        applyFilters()
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
        applyFilters()
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    private fun applyFilters() {
        val query = _searchQuery.value
        val category = _selectedCategory.value

        var filtered = allTests

        if (category != "All") {
            filtered = filtered.filter { it.name.equals(category, ignoreCase = true) }
        }

        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = UiState.Success(filtered)
    }
}
