package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
	@field:SerializedName("data")
	val data: DataDashboard? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Visitors(

	@field:SerializedName("product")
	val product: Int? = null,

	@field:SerializedName("visitor")
	val visitor: Int? = null,

	@field:SerializedName("category")
	val category: Int? = null,

	@field:SerializedName("total_click")
	val totalClick: Int? = null
)

data class DataDashboard(
	@field:SerializedName("visitors")
	val visitors: Visitors? = null
)
