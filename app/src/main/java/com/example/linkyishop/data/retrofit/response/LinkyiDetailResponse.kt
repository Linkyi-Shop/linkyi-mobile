package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class LinkyiDetailResponse(

	@field:SerializedName("data")
	val data: LinkyiDetailResponseData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LinkyiDetailResponseData(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
