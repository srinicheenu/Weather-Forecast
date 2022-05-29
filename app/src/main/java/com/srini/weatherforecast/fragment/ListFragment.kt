package com.srini.weatherforecast.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.srini.weatherforecast.adapter.WeatherListAdapter
import com.srini.weatherforecast.databinding.FragmentListBinding
import com.srini.weatherforecast.viewmodel.WeatherViewModel

class ListFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerview.layoutManager = LinearLayoutManager(requireActivity())

        weatherViewModel.getCity().observe(viewLifecycleOwner, Observer {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it +" - Hourly weather"
        })
        getWeatherData()
    }

    private fun getWeatherData() {

        weatherViewModel.getWeatherData().observe(this.requireActivity(), Observer {
            if( it != null ) {
                binding.recyclerview.adapter =
                    WeatherListAdapter(it.forecast?.forecastday?.get(0)?.hour) { hourItems ->

                        weatherViewModel.setHourlyData(hourItems)
                        val navDirections =
                            ListFragmentDirections.actionListFragmentToDetailFragment()
                        Navigation.findNavController(binding.recyclerview).navigate(navDirections)
                    }
            }
        })
    }
}