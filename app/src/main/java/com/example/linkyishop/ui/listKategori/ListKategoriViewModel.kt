package com.example.linkyishop.ui.listKategori

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.DataItemKategori
import kotlinx.coroutines.launch

class ListKategoriViewModel(private val repository: UserRepository) : ViewModel() {
    private val _categories = MutableLiveData<List<DataItemKategori>>()
    val categories: LiveData<List<DataItemKategori>> get() = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getCategory()
                if (response.success == true) {
                    _categories.value = (response.data?.productCategories?.data ?: emptyList()) as List<DataItemKategori>?
                } else {
                    _error.value = response.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                Log.e("ListKategoriViewModel", "Failed to fetch categories", e)
                _error.value = e.message ?: "Failed to fetch categories"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

