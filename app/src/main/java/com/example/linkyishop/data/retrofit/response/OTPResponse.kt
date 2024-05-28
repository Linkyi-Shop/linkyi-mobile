package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class OTPResponse(

	@field:SerializedName("OtpResult")
	val OtpResult: ORPResult? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ORPResult(
	@field:SerializedName("token")
	val token: String? = null
)
