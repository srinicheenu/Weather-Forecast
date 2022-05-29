package com.srini.weatherforecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.srini.weatherforecast.model.Hour
import com.srini.weatherforecast.model.Weather
import com.srini.weatherforecast.retrofit.Common.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Shared View Model for three Fragments
class WeatherViewModel : ViewModel() {

    private val key = "3444b7679ba94113ab592316222401"
    private val weatherMLiveData: MutableLiveData<Weather> = MutableLiveData()
    private val cityMLiveData: MutableLiveData<String> = MutableLiveData()
    private val hourlyLiveData: MutableLiveData<Hour> = MutableLiveData()
    val errorMessage = MutableLiveData<Response<Weather>>()
    val loading = MutableLiveData<Boolean>()

    fun getWeather(cityName: String): LiveData<Weather> {

        loading.value = true

        retrofitService.getWeather(key, cityName).enqueue(object :
            Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    loading.value = false
                    weatherMLiveData.postValue(response.body())
                } else {
                    onError(response)
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
            }
        })
        return weatherMLiveData
    }

    fun getWeatherData(): MutableLiveData<Weather> = weatherMLiveData

    fun setWeatherData(weather: Weather?) {
        weatherMLiveData.value = weather
    }

    fun getHourlyData(): MutableLiveData<Hour> = hourlyLiveData

    fun setHourlyData(hour: Hour?) {
        hourlyLiveData.value = hour
    }

    fun getCity(): MutableLiveData<String> = cityMLiveData

    fun setCity(city: String) {
        cityMLiveData.value = city
    }

    private fun onError(message: Response<Weather>) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}