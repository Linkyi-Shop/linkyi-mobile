package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class CheckEmailResponse(

	@field:SerializedName("data")
	val data: DataEmail? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataEmail(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Any? = null
)
