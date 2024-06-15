package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: DataProfile? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profile(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("google_login")
	val googleLogin: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Store(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("verified")
	val verified: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class DataProfile(

	@field:SerializedName("profile")
	val profile: Profile? = null,

	@field:SerializedName("store")
	val store: Store? = null
)
