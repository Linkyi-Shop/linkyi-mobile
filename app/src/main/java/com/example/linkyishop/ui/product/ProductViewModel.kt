package com.example.linkyishop.ui.product

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkyishop.BuildConfig
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.data.preferences.dataStore
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import com.example.linkyishop.data.retrofit.api.ApiServices
import com.example.linkyishop.data.retrofit.response.DataItem
import com.example.linkyishop.utils.Event
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listProduct = MutableLiveData<List<DataItem?>?>()
    val listProduct: LiveData<List<DataItem?>?> = _listProduct

    suspend fun getProducts(){
        try {
            val token = repository.getUserToken()
            val response = ApiConfig.getApiService().getProducts("Bearer $token")
            if (response.isSuccessful) {
                _listProduct.postValue(response.body()?.data?.links?.data)
                Log.e("Products", "Isi: ${response.body()?.data?.links?.data}")
            } else {
                Log.e("Products", "Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Products", "Exception: ${e.message.toString()}")
        }
    }
}