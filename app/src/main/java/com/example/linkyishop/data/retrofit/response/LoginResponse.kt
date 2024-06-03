package com.example.linkyishop.data.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
@Parcelize
data class DataLogin (

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("token")
	val token: String
) : Parcelable
