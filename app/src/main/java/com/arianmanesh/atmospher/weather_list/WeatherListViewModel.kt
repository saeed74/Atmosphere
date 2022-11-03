package com.arianmanesh.atmospher.weather_list

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arianmanesh.atmospher.WeatherItemResponse
import com.arianmanesh.atmospher.core.ResponseResult
import com.arianmanesh.atmospher.database.CitiesDBModel
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.SocketTimeoutException


class WeatherListViewModel(application: Application) : AndroidViewModel(application) {

    private val context : Application = application
    private var toDeletePosition : Int = 0
    //lateinit var deleteJob : Job

    private val _weatherData = MutableLiveData<ResponseResult<WeatherItemResponse>>()
    val weatherData: LiveData<ResponseResult<WeatherItemResponse>>
        get() = _weatherData

    private val _citiesData = MutableLiveData<ResponseResult<List<CitiesDBModel>>>()
    val citiesData: LiveData<ResponseResult<List<CitiesDBModel>>>
        get() = _citiesData

    private val _cityDelete = MutableLiveData<ResponseResult<CitiesDBModel>>()
    val cityDelete: LiveData<ResponseResult<CitiesDBModel>>
        get() = _cityDelete

    private val repository = WeatherListRepository()

    fun updateWeather(city: String) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                _weatherData.postValue(ResponseResult.Loading())
                when (val result = repository.getCityWeather(city)){
                    is ResponseResult.Success ->{
                        _weatherData.postValue(result)
                    }
                    is ResponseResult.Error ->{
                        _weatherData.postValue(result)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                if(e is SocketTimeoutException) {
                    _weatherData.postValue(ResponseResult.Error(HttpURLConnection.HTTP_GATEWAY_TIMEOUT))
                }
            }

        }

    }

    fun getAllCitiesFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _citiesData.postValue(ResponseResult.Loading())
            _citiesData.postValue(repository.readAllCitiesFromDB(context))
        }
    }

    fun removeCityFromDB(city: CitiesDBModel , pos : Int){
//        if(this::deleteJob.isInitialized && deleteJob.isActive){
//            deleteJob.cancel()
//        }
//        deleteJob = Job()
        viewModelScope.launch(Dispatchers.IO) {
            _cityDelete.postValue(ResponseResult.Loading())
            toDeletePosition = pos
            _cityDelete.postValue(repository.removeCityFromDB(context,city))
            //_cityDelete.postValue(repository.readAllCitiesFromDB(context))
        }
    }

    fun getRemovedPosition(): Int{
        return toDeletePosition
    }

    //todo: mig to db
//    fun getCurrentSelectedCity(context: Context): String{
//        return repository.getCurrentSelectedCity(context)
//    }
//
//    fun storeCurrentSelectedCity(city: String,context: Context){
//        repository.storeCurrentSelectedCity(city,context)
//    }



}