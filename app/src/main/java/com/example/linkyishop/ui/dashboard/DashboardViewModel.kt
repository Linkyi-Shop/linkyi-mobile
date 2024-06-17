package com.example.linkyishop.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.BuildConfig
import com.example.linkyishop.data.retrofit.api.ApiServices
import com.example.linkyishop.data.retrofit.response.Visitors
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardViewModel : ViewModel() {
    private val _dashboardData = MutableLiveData<Visitors>()
    val dashboardData: LiveData<Visitors> get() = _dashboardData

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)  // Ganti dengan base URL API Anda
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiServices::class.java)

    fun fetchDashboardData(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getAnalyzeStore("Bearer $token")
                if (response.success == true) {
                    _dashboardData.value = response.data?.visitors!!
                }
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}