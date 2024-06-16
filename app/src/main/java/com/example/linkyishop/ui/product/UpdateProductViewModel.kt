package com.example.linkyishop.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.ProductStatusResponse
import com.example.linkyishop.data.retrofit.response.UpdateProductResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _updateProductResult = MutableLiveData<Result<UpdateProductResponse>>()
    val updateProductResult: LiveData<Result<UpdateProductResponse>> = _updateProductResult

    private val _updateStatusResult = MutableLiveData<Result<ProductStatusResponse>>()
    val updateStatusResult: LiveData<Result<ProductStatusResponse>> = _updateStatusResult

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

    fun productStatus(
        productId: String,
        isActive: String
    ) {
        viewModelScope.launch {
            try {
                repository.productStatus(productId, isActive)
            } catch (e: Exception) {
                Log.e("UpdateProductViewModel", "Error updating product status: ${e.message}")
            }
        }
    }
}
