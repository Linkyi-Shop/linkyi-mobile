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

class ProductViewModel(application: Application) :  AndroidViewModel(application) {

    private val _listProduct = MutableLiveData<Event<List<DataItem?>?>>()
    val listProduct: LiveData<Event<List<DataItem?>?>> = _listProduct

    init {
        // Panggil API dengan token
        val token = UserPreference.TOKEN_KEY
        val client = ApiConfig.getApiService().getProducts("Bearer $token")
        client.enqueue(object : Callback<List<DataItem>> {
            override fun onResponse(call: Call<List<DataItem>>, response: Response<List<DataItem>>) {
                if (response.isSuccessful) {
                    _listProduct.value = Event(response.body())
                }
            }

            override fun onFailure(call: Call<List<DataItem>>, t: Throwable) {
                Log.e("Products", "onFailure: ${t.message.toString()}")
            }
        })
    }
}