package com.example.movieappv2.data.api

import com.example.movieappv2.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// RetrofitInstance để khởi tạo một đối tượng Retrofit duy nhất
object RetrofitInstance {
    // Tạo một đối tượng Retrofit, chỉ tạo một lần duy nhất
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}