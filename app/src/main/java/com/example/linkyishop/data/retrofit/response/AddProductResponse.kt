package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class AddProductResponse(

	@field:SerializedName("data")
	val data: DataActive? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataActive(

	@field:SerializedName("is_active")
	val isActive: String? = null
)
