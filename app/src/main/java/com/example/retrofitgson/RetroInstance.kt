package com.example.retrofitgson

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {

    companion object {
        const val BASEURL = "https://www.superheroapi.com/api.php/10217754960900715/"
        fun getRetroInstance(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}