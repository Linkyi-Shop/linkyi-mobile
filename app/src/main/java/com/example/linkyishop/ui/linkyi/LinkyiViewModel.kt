package com.example.linkyishop.ui.linkyi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.AddLinkyiResponse
import com.example.linkyishop.data.retrofit.response.DetailProductResponse
import com.example.linkyishop.data.retrofit.response.Links
import com.example.linkyishop.data.retrofit.response.LinksDataItem
import com.example.linkyishop.data.retrofit.response.LinkyiDetailResponse
import com.example.linkyishop.data.retrofit.response.LinkyiResponse
import kotlinx.coroutines.launch

class LinkyiViewModel(private val repository: UserRepository) : ViewModel(){

    private val _linkyi = MutableLiveData<Links?>()
    val linkyi: LiveData<Links?> get() = _linkyi

    private val _linkyiDetail = MutableLiveData<LinkyiDetailResponse?>()
    val linkyiDetail: LiveData<LinkyiDetailResponse?> get() = _linkyiDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _linkyiResponse = MutableLiveData<AddLinkyiResponse?>()
    val linkyiResponse: LiveData<AddLinkyiResponse?> get() = _linkyiResponse

    fun getLinkyi() {
        viewModelScope.launch {
            try {
                val response = repository.getLinkyi()
                _linkyi.value = response.data?.links
            } catch (e: Exception) {
                Log.e("Failed to fetch product detail", e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun getLinkyi(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getLinkyi(id)
                _linkyiDetail.value = response
            } catch (e: Exception) {
                Log.e("Failed to fetch product detail", e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun addLink(link: String, name: String, is_active: String) {
        viewModelScope.launch {
            try {
                val response = repository.addLinkyi(link, "link", name, is_active)
                _linkyiResponse.value = response
            } catch (e: Exception) {
                Log.e("Failed to fetch product detail", e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun addHeadline(name: String, is_active: String) {
        viewModelScope.launch {
            try {
                val response = repository.addLinkyi(null, "headline", name, is_active)
                _linkyiResponse.value = response
            } catch (e: Exception) {
                Log.e("Failed to fetch product detail", e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }
}