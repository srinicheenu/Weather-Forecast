package com.srini.weatherforecast.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.srini.weatherforecast.databinding.FragmentSearchBinding
import com.srini.weatherforecast.viewmodel.WeatherViewModel

class SearchFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.btnSearch.setOnClickListener {

            weatherViewModel.getWeatherData().postValue(null)
            val text: String = binding.tilWeather.editText?.text.toString().trim()
            weatherViewModel.setCity(text)

            if (isNetworkAvailable(requireActivity())) {
                searchCity(text)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Internet connection required!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Weather Forecast"

        weatherViewModel.getCity().observe(viewLifecycleOwner, Observer {
            binding.tilWeather.editText?.setText(it)
        })

        weatherViewModel.errorMessage.observe(requireActivity()) {

            if (it.raw().code() == 200) {
                Toast.makeText(requireActivity(), "City not found!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        weatherViewModel.loading.observe(requireActivity(), Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })
    }

    private fun searchCity(city: String) {
        if (city.isNotEmpty()) {
            weatherViewModel.getWeather(city).observe(viewLifecycleOwner, Observer {
                if( it != null) {
                    val navDirections =
                        SearchFragmentDirections.actionSearchFragmentToListFragment()
                    Navigation.findNavController(binding.btnSearch).navigate(navDirections)
                }
            })
        } else {
            Toast.makeText(requireActivity(), "Enter any city name!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}

