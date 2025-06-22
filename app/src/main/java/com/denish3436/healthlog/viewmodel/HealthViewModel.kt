package com.denish3436.healthlog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.denish3436.healthlog.data.database.HealthEntry
import com.denish3436.healthlog.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HealthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class HealthViewModel(private val repository: HealthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    private val _recentEntries = MutableStateFlow<List<HealthEntry>>(emptyList())
    val recentEntries: StateFlow<List<HealthEntry>> = _recentEntries.asStateFlow()

    init {
        loadRecentEntries()
    }

    fun loadRecentEntries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getAllEntries().collect { entries ->
                    _recentEntries.value = entries.take(10)
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load entries: ${e.message}"
                )
            }
        }
    }

    fun addHealthEntry(entry: HealthEntry) {
        viewModelScope.launch {
            try {
                repository.insertEntry(entry)
                loadRecentEntries()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to save entry: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

class HealthViewModelFactory(private val repository: HealthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}