package com.srini.weatherforecast.retrofit

import RetrofitClient
import RetrofitService

object Common {

    var BASE_URL = "http://api.weatherapi.com/"

    val retrofitService: RetrofitService
        get() = RetrofitClient.getRetrofit(BASE_URL).create(RetrofitService::class.java)
}