package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class NewPasswordResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("reset_pass_token")
	val resetPassToken: String? = null,

	@field:SerializedName("confirm_password")
	val confirmPassword: String? = null
)
