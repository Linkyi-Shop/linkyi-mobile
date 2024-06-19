package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class KategoriResponse(
	@SerializedName("success")
	val success: Boolean?,

	@SerializedName("data")
	val data: DataKategori?,

	@SerializedName("message")
	val message: String?
)

data class DataKategori(
	@SerializedName("product_categories")
	val productCategories: ProductCategories?
)

data class ProductCategories(
	@SerializedName("current_page")
	val currentPage: Int?,

	@SerializedName("data")
	val data: List<DataItemKategori?>?,

	@SerializedName("first_page_url")
	val firstPageUrl: String?,

	@SerializedName("from")
	val from: Int?,

	@SerializedName("last_page")
	val lastPage: Int?,

	@SerializedName("last_page_url")
	val lastPageUrl: String?,

	@SerializedName("links")
	val links: List<Link?>?,

	@SerializedName("next_page_url")
	val nextPageUrl: String?,

	@SerializedName("path")
	val path: String?,

	@SerializedName("per_page")
	val perPage: Int?,

	@SerializedName("prev_page_url")
	val prevPageUrl: String?,

	@SerializedName("to")
	val to: Int?,

	@SerializedName("total")
	val total: Int?
)

data class DataItemKategori(
	@SerializedName("id")
	val id: String?,

	@SerializedName("name")
	val name: String?,

	@SerializedName("slug")
	val slug: String?,

	@SerializedName("is_active")
	val isActive: Boolean?,

	@SerializedName("sequence")
	val sequence: Int?,

	@SerializedName("total_product")
	val totalProduct: Int?
)

data class Link(
	@SerializedName("url")
	val url: String?,

	@SerializedName("label")
	val label: String?,

	@SerializedName("active")
	val active: Boolean?
)
