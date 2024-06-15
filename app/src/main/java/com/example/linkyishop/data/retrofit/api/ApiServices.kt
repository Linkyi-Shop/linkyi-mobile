package com.example.linkyishop.data.retrofit.api

import com.example.linkyishop.data.retrofit.response.AddProductResponse
import com.example.linkyishop.data.retrofit.response.AktivasiTokoResponse
import com.example.linkyishop.data.retrofit.response.CekUsernameResponse
import com.example.linkyishop.data.retrofit.response.DeleteProductResponse
import com.example.linkyishop.data.retrofit.response.DetailProductResponse
import com.example.linkyishop.data.retrofit.response.LoginResponse
import com.example.linkyishop.data.retrofit.response.LupaPasswordResponse
import com.example.linkyishop.data.retrofit.response.NewPassword2Response
import com.example.linkyishop.data.retrofit.response.OTPResponse
import com.example.linkyishop.data.retrofit.response.ProductsResponse
import com.example.linkyishop.data.retrofit.response.ProfileResponse
import com.example.linkyishop.data.retrofit.response.RegisterResponse
import com.example.linkyishop.data.retrofit.response.ResendOtpResponse
import com.example.linkyishop.data.retrofit.response.UpdatePasswordResponse
import com.example.linkyishop.data.retrofit.response.UpdateProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
    fun OTP(
        @Field("code") code: Int,
        @Field("email") email: String?
    ): Call<OTPResponse>

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

    @Multipart
    @POST("dashboard/products/create")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("is_active") isActive: RequestBody,
        @Part links: List<MultipartBody.Part>
    ): Response<DeleteProductResponse>

    @GET("dashboard/products/{id}")
    suspend fun getProductDetail(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): DetailProductResponse

    @DELETE("dashboard/products/delete/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): DeleteProductResponse

    @FormUrlEncoded
    @POST("dashboard/products/{id}/link/create")
    suspend fun addProductLink(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Field("link") link: String
    ): DeleteProductResponse

    @DELETE("dashboard/products/{id}/link/delete/{link}")
    suspend fun deleteLinkProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Path("link") linkId: String
    ): DeleteProductResponse

    @FormUrlEncoded
    @POST("profile/check-username")
    suspend fun checkUsername(
        @Header("Authorization") token: String,
        @Field("username") username: String
    ): CekUsernameResponse
    @Multipart
    @POST("profile/store-activation")
    suspend fun activateStore(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("description") description: RequestBody,
        @Part logo: MultipartBody.Part
    ): AktivasiTokoResponse

    @FormUrlEncoded
    @POST("profile/update-password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("current_password") currentPassword: String
    ): UpdatePasswordResponse

    @Multipart
    @POST("dashboard/products/update/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part thumbnail: MultipartBody.Part?
    ): UpdateProductResponse
    @GET("profile")
    suspend fun getStoreProfile(): ProfileResponse
}