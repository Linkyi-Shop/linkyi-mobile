package com.example.linkyishop.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.UpdateProductResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _updateProductResult = MutableLiveData<Result<UpdateProductResponse>>()
    val updateProductResult: LiveData<Result<UpdateProductResponse>> = _updateProductResult

    fun updateProduct(
        productId: String,
        title: RequestBody,
        price: RequestBody,
        category: RequestBody,
        thumbnail: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                val response = repository.updateProduct(productId, title, price, category, thumbnail)
                _updateProductResult.value = Result.success(response)
            } catch (e: Exception) {
                _updateProductResult.value = Result.failure(e)
            }
        }
    }
}
