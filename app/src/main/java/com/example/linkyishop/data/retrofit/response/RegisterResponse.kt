package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("registerResult")
	val registerResult: RegisterResult? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RegisterResult(

	@field:SerializedName("email")
	val email: String? = null,
)
