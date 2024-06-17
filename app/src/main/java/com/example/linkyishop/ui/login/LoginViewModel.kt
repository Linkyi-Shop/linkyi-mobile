package com.example.linkyishop.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.preferences.UserModel
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import com.example.linkyishop.data.retrofit.response.DataLogin
import com.example.linkyishop.data.retrofit.response.LoginResponse
import com.example.linkyishop.data.retrofit.response.Profile
import com.example.linkyishop.data.retrofit.response.ProfileResponse
import com.example.linkyishop.data.retrofit.response.Store
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<DataLogin>()
    val loginResult: LiveData<DataLogin> = _loginResult

    private val _profileData = MutableLiveData<Store?>()
    val profileData: LiveData<Store?> = _profileData

    fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _loginResult.value = response.body()?.data
                } else {
//                    _message.value = Event("Tidak Ditemukan")
                    Log.e("Loginewe", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                _isLoading.value = false
                Log.e("Loginewe", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getStoreProfile("Bearer $token")
                if (response.success == true && response.data?.store != null) {
                    Log.d("ProfileViewModel", "Store Data: ${response.data.store}")
                    _profileData.value = response.data.store
                } else {
                    Log.e("ProfileViewModel", "Error fetching profile: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Exception fetching profile", e)
            }
        }
    }
    fun saveUserToken(token: String) {
        viewModelScope.launch {
            repository.saveUserToken(token)
        }
    }
    fun getUserToken() {
        viewModelScope.launch {
            repository.getUserToken()
        }
    }

    fun deleteUserToken() {
        viewModelScope.launch {
            repository.deleteToken()
        }
    }
}