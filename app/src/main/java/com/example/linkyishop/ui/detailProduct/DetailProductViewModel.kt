package com.example.linkyishop.ui.detailProduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.DeleteProductResponse
import com.example.linkyishop.data.retrofit.response.DetailProductResponse
import kotlinx.coroutines.launch

class DetailProductViewModel(private val repository: UserRepository) : ViewModel() {

    private val _productDetail = MutableLiveData<DetailProductResponse>()
    val productDetail: LiveData<DetailProductResponse> get() = _productDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _deleteResponse = MutableLiveData<DeleteProductResponse>()
    val deleteResponse: LiveData<DeleteProductResponse> get() = _deleteResponse

    fun fetchProductDetail(productId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getProductDetail(productId)
                _productDetail.value = response
            } catch (e: Exception) {
                _error.value = "Failed to fetch product detail: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(productId: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.deleteProduct(productId)
                _deleteResponse.value = response
            } catch (e: Exception) {
                _error.value = "Failed to fetch product detail: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun addLink(productId: String, link: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addLinkProduct(productId, link)
                _deleteResponse.value = response
            } catch (e: Exception) {
                _error.value = "Failed to fetch product detail: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteLink(productId: String, linkId: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.deleteLinkProduct(productId, linkId)
                _deleteResponse.value = response
            } catch (e: Exception) {
                _error.value = "Failed to fetch product detail: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

//    fun activateProduct(productId: String){
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val response = repository.deleteProduct(productId)
//                _deleteResponse.value = response
//            } catch (e: Exception) {
//                _error.value = "Failed to fetch product detail: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
}