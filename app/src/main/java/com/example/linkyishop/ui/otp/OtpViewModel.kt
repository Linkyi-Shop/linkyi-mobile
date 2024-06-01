package com.example.linkyishop.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.OTPResponse
import com.example.linkyishop.data.retrofit.response.ResendOtpResponse
import kotlinx.coroutines.launch

class OtpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _otpResult = MutableLiveData<Result<OTPResponse>>()
    val otpResult: LiveData<Result<OTPResponse>> = _otpResult

    private val _resendOtpResult = MutableLiveData<Result<ResendOtpResponse>>()
    val resendOtpResult: LiveData<Result<ResendOtpResponse>> = _resendOtpResult

    fun verifyOtp(code: Int, email: String?) {
        viewModelScope.launch {
            try {
                val response = repository.otpVerification(code, email)
                _otpResult.value = Result.success(response)
            } catch (e: Exception) {
                _otpResult.value = Result.failure(e)
            }
        }
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