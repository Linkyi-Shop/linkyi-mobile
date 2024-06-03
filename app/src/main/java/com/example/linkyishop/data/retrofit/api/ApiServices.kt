package com.example.linkyishop.data.retrofit.api

import com.example.linkyishop.data.retrofit.response.DataItem
import com.example.linkyishop.data.retrofit.response.LoginResponse
import com.example.linkyishop.data.retrofit.response.LupaPasswordResponse
import com.example.linkyishop.data.retrofit.response.NewPassword2Response
import com.example.linkyishop.data.retrofit.response.NewPasswordResponse
import com.example.linkyishop.data.retrofit.response.OTPResponse
import com.example.linkyishop.data.retrofit.response.ProductsResponse
import com.example.linkyishop.data.retrofit.response.RegisterResponse
import com.example.linkyishop.data.retrofit.response.ResendOtpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("auth/otp-confirmation")
    suspend fun OTP(
        @Field("code") code: Int,
        @Field("email") email: String?
    ): OTPResponse

    @FormUrlEncoded
    @POST("auth/otp-confirmation-resend")
    suspend fun resendOTP(
        @Field("email") email: String?
    ): ResendOtpResponse

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun lupapassword(
        @Field("email") email: String
    ): LupaPasswordResponse

    @FormUrlEncoded
    @POST("auth/new-password")
    suspend fun newPassword(
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("reset_pass_token") otp: Int
    ): NewPassword2Response

    @GET("dashboard/products/")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<ProductsResponse>
}