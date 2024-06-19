package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("decision")
	val decision: String,

	@field:SerializedName("prediction")
	val prediction: List<List<List<Any>>>,

	@field:SerializedName("max_probability")
	val maxProbability: Any
)
