package com.example.linkyishop.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.OTPResponse
import kotlinx.coroutines.launch

class OtpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _otpResult = MutableLiveData<Result<OTPResponse>>()
    val otpResult: LiveData<Result<OTPResponse>> = _otpResult

    fun verifyOtp(code: Int) {
        viewModelScope.launch {
            try {
                val response = repository.otpVerification(code)
                _otpResult.value = Result.success(response)
            } catch (e: Exception) {
                _otpResult.value = Result.failure(e)
            }
        }
    }
}