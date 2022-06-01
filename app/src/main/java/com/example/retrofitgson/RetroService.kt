package com.example.retrofitgson

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RetroService {
    @GET
    fun getHero(@Url url: String?): Call<HeroModel>

    @GET
    fun getHeroStats(@Url url: String?): Call<ResponseBody>
}