package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class CekUsernameResponse(

	@field:SerializedName("data")
	val data: DataUsername? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataUsername(

	@field:SerializedName("availability")
	val availability: Boolean? = null,

	@field:SerializedName("username")
	val username: String? = null
)
