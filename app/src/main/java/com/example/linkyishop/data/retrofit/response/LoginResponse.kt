package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginResult (

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("token")
	val token: Any? = null
)
