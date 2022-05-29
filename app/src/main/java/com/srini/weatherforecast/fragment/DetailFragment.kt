package com.srini.weatherforecast.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srini.weatherforecast.R
import com.srini.weatherforecast.adapter.WeatherListAdapter
import com.srini.weatherforecast.databinding.FragmentDetailBinding
import com.srini.weatherforecast.databinding.FragmentListBinding
import com.srini.weatherforecast.viewmodel.WeatherViewModel

class DetailFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        weatherViewModel.getCity().observe(viewLifecycleOwner, Observer {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it +" - Weather Details"
        })

        getWeatherData()
    }

    private fun getWeatherData() {

        weatherViewModel.getHourlyData().observe(this.requireActivity(), Observer {

            binding.tempTv.text = it.tempC.toString()
            binding.feelsLikeTv.text = "Feels like "+it.feelslikeC.toString()
            binding.weather.text = it.condition?.text
        })
    }
}
