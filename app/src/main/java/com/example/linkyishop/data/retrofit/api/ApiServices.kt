package com.example.linkyishop.data.retrofit.api

import com.example.linkyishop.data.retrofit.response.LoginResponse
import com.example.linkyishop.data.retrofit.response.OTPResponse
import com.example.linkyishop.data.retrofit.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/otp-confirmation")
    suspend fun OTP(
        @Field("code") code: Int
    ): OTPResponse
}