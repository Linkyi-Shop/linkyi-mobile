package com.example.linkyishop.data.repository

import com.example.linkyishop.data.preferences.UserModel
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.data.retrofit.api.ApiServices
import com.example.linkyishop.data.retrofit.response.LoginResponse
import com.example.linkyishop.data.retrofit.response.LupaPasswordResponse
import com.example.linkyishop.data.retrofit.response.NewPassword2Response
import com.example.linkyishop.data.retrofit.response.OTPResponse
import com.example.linkyishop.data.retrofit.response.RegisterResponse
import com.example.linkyishop.data.retrofit.response.ResendOtpResponse
import kotlinx.coroutines.flow.Flow


class UserRepository private constructor(private val pref: UserPreference, private val apiServices: ApiServices) {
    fun getUser(): Flow<UserModel> {
        return pref.getUser()
    }

    suspend fun register(name: String, email: String, password: String) : RegisterResponse {
        return apiServices.register(name, email, password)
    }

    suspend fun login(email: String, password: String) : LoginResponse {
        return apiServices.login(email, password)
    }

    suspend fun otpVerification(code: Int, email: String?) : OTPResponse {
        return apiServices.OTP(code, email)
    }

    suspend fun resendOtp(email: String?): ResendOtpResponse {
        return apiServices.resendOTP(email)
    }

    suspend fun lupaPassword(email: String): LupaPasswordResponse {
        return apiServices.lupapassword(email)
    }

    suspend fun newPassword(password: String, confirmPassword: String, otp: Int): NewPassword2Response {
        return apiServices.newPassword(password, confirmPassword, otp)
    }

    suspend fun saveUserToken(token: String) {
        pref.saveToken(token)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            pref: UserPreference,
            apiServices: ApiServices
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(pref, apiServices)
            }.also { instance = it }
    }
}