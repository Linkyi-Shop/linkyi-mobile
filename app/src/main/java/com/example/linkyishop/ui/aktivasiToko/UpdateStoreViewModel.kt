package com.example.linkyishop.ui.aktivasiToko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.UpdateTokoResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateStoreViewModel(private val repository: UserRepository): ViewModel() {
    private val _updateStoreResult = MutableLiveData<Result<UpdateTokoResponse>>()
    val updateStoreResult: LiveData<Result<UpdateTokoResponse>> = _updateStoreResult

    fun updateStore(
        name: RequestBody,
        description: RequestBody?,
        logo: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateStore(name, description, logo)
                _updateStoreResult.value = Result.success(response)
            } catch (e: Exception) {
                _updateStoreResult.value = Result.failure(e)
            }
        }
    }
}