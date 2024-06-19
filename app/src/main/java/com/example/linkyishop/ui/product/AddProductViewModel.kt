package com.example.linkyishop.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import com.example.linkyishop.data.retrofit.response.AddProductResponse
import com.example.linkyishop.data.retrofit.response.DeleteProductResponse
import com.example.linkyishop.data.retrofit.response.PredictionResponse
import com.example.linkyishop.data.retrofit.response.ProductsResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _addProductResult = MutableLiveData<DeleteProductResponse>()
    val addProductResult: LiveData<DeleteProductResponse> = _addProductResult

    private val _predictionResult = MutableLiveData<PredictionResponse>()
    val predictionResult: LiveData<PredictionResponse> = _predictionResult

    fun addProduct(
        title: String,
        price: String,
        category: String,
        thumbnail: MultipartBody.Part,
        isActive: String,
        links: List<String>
    ) {
        viewModelScope.launch {
            try {
                val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
                val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())
                val isActivePart = isActive.toRequestBody("text/plain".toMediaTypeOrNull())
                val linksParts = links.mapIndexed { index, link ->
                    MultipartBody.Part.createFormData("links[$index]", link)
                }

                val response = repository.addProduct(
                    titlePart, pricePart, categoryPart, thumbnail, isActivePart, linksParts
                )
                _addProductResult.value = response
                Log.e("AddProduct", "Success: ${response}")
            } catch (e: Exception) {
                Log.e("AddProduct", "Exception: ${e.message.toString()}")
            }
        }
    }

    fun predictImage(
        thumbnail: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiServiceMl().predictImage("kokojiji", thumbnail)
                _predictionResult.value = response
                Log.e("AddProduct", "Success: ${response}")
            } catch (e: Exception) {
                Log.e("AddProduct", "Exception: ${e.message.toString()}")
            }
        }
    }

    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create(
            "image/jpeg".toMediaType(), file
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}
