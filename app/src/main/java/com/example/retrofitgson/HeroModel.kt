package com.example.retrofitgson

data class HeroModel(
    val response: String,
    val id: Int?,
    val name: String?,
    val image: HeroImage
)

data class HeroImage(val url: String?)



