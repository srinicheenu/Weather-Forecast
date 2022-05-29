import com.srini.weatherforecast.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("/v1/forecast.json?")
    fun getWeather(
        @Query("key") key: String,
        @Query("q") query: String
    ): Call<Weather>
}