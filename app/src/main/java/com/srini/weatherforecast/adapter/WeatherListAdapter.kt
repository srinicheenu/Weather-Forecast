package com.srini.weatherforecast.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.srini.weatherforecast.databinding.ItemWeatherBinding
import com.srini.weatherforecast.model.Hour
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class WeatherListAdapter(
    private val mList: ArrayList<Hour>?,
    private val onItemClicked: (Hour?) -> Unit
) : RecyclerView.Adapter<WeatherListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {

            val hourItems = mList?.get(position)
            Picasso.get().load("https:" + hourItems?.condition?.icon).into(binding.icHourlyStatus)

            // sets the text to the textview from our itemHolder class
            binding.hourlyTemperature.text = hourItems?.tempC.toString()
            binding.hourTextview.text = hourItems?.time.toString()
            binding.feelsLike.text = "Feels Like " + hourItems?.feelslikeC.toString()

            binding.root.setOnClickListener {
                onItemClicked(hourItems)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    fun parseDate() {
        var formatter: DateTimeFormatter? = null
        val date = "2021-11-03T14:09:31.135Z" // your date string
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX") // formatter
            val dateTime: ZonedDateTime = ZonedDateTime.parse(date, formatter) // date object
            val formatter2: DateTimeFormatter =
                DateTimeFormatter.ofPattern("EEEE, MMM d : HH:mm") // if you want to convert it any other format
            Log.e("Date", "" + dateTime.format(formatter2))
        }
    }

}

