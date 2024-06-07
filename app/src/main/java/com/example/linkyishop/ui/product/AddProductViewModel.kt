package com.example.linkyishop.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import com.example.linkyishop.data.retrofit.response.ProductsResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _addProductResult = MutableLiveData<Result<ProductsResponse>>()
    val addProductResult: LiveData<Result<ProductsResponse>> = _addProductResult

    suspend fun addProduct(
        title: String,
        price: String,
        category: String,
        thumbnail: File,
        isActive: String,
        links: List<String>
    ) {
        try {
            val token = repository.getUserToken()
            val titlePart = createPartFromString(title)
            val pricePart = createPartFromString(price)
            val categoryPart = createPartFromString(category)
            val thumbnailPart = prepareFilePart("thumbnail", thumbnail)
            val isActivePart = createPartFromString(isActive) // Use Boolean directly
            val linksParts = links.map { createPartFromString(it) }

            val response = ApiConfig.getApiService().addProduct(
                "Bearer $token", titlePart, pricePart, categoryPart, thumbnailPart, isActivePart, linksParts
            )
            if (response.isSuccessful) {
                _addProductResult.postValue(Result.success(response.body()!!))
                Log.e("AddProduct", "Success: ${response.body()}")
            } else {
                _addProductResult.postValue(Result.failure(Exception(response.message())))
                Log.e("AddProduct", "Error: ${response.message()}")
            }
        } catch (e: Exception) {
            _addProductResult.postValue(Result.failure(e))
            Log.e("AddProduct", "Exception: ${e.message.toString()}")
        }
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            okhttp3.MultipartBody.FORM, descriptionString
        )
    }

    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create(
            "image/jpeg".toMediaType(), file
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}
