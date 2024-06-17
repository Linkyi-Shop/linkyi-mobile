package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class UpdateTokoResponse(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
