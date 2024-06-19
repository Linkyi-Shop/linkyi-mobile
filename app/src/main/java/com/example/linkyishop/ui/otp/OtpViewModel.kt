package com.example.linkyishop.ui.otp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import com.example.linkyishop.data.retrofit.response.OTPResponse
import com.example.linkyishop.data.retrofit.response.ResendOtpResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _otpResult = MutableLiveData<OTPResponse>()
    val otpResult: LiveData<OTPResponse> = _otpResult

    private val _resendOtpResult = MutableLiveData<Result<ResendOtpResponse>>()
    val resendOtpResult: LiveData<Result<ResendOtpResponse>> = _resendOtpResult

    fun verifyOTP(code: Int, email: String?) {
        val client = ApiConfig.getApiService().OTP(code, email)
        client.enqueue(object : Callback<OTPResponse> {
            override fun onResponse(
                call: Call<OTPResponse>,
                response: Response<OTPResponse>
            ) {
                if (response.isSuccessful) {
                    _otpResult.value = response.body()
                } else {
//                    _message.value = Event("Tidak Ditemukan")
                    Log.e("Loginewe", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
//                _isLoading.value = false
                Log.e("Loginewe", "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun resendOtp(email: String?) {
        viewModelScope.launch {
            try {
                val result = repository.resendOtp(email)
                _resendOtpResult.value = Result.success(result)
            } catch (e: Exception) {
                _resendOtpResult.value = Result.failure(e)
            }
        }
    }
}