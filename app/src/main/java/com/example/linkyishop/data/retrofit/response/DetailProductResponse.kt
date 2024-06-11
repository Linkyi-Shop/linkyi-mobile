package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class DetailProductResponse(

	@field:SerializedName("data")
	val data: DataDetail? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LinksItemDetail(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class DataDetail(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("links")
	val links: List<LinksItemDetail?>? = null,

	@field:SerializedName("title")
	val title: String? = null
)
