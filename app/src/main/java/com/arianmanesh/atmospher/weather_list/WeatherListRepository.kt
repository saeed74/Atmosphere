package com.arianmanesh.atmospher.weather_list

import android.content.Context
import android.util.Log
import com.arianmanesh.atmospher.R
import com.arianmanesh.atmospher.WeatherItemResponse
import com.arianmanesh.atmospher.core.ApiService
import com.arianmanesh.atmospher.core.ResponseResult
import com.arianmanesh.atmospher.core.RetrofitInstance
import com.arianmanesh.atmospher.database.AtmosphereDataBase
import com.arianmanesh.atmospher.database.CitiesDBModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WeatherListRepository () {

    suspend fun getCityWeather(
        city: String
    ): ResponseResult<WeatherItemResponse> {

        val response = RetrofitInstance.api.getWeatherDetail(RetrofitInstance.apiKey,city)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return ResponseResult.Success(body)
            }
        }
        return ResponseResult.Error(response.code(),response.errorBody())

    }

    fun readAllCitiesFromDB(context: Context): ResponseResult<List<CitiesDBModel>>{
        return ResponseResult.Success(AtmosphereDataBase.getInstance(context).citiesDao().getAllCities())
    }

    fun removeCityFromDB(context: Context, city: CitiesDBModel): ResponseResult<CitiesDBModel>{
        if(AtmosphereDataBase.getInstance(context).citiesDao().getCurrentSelectedCity().name == city.name){
            return ResponseResult.DataBaseError(context.getString(R.string.cant_delete_current_city))
        }else{
            AtmosphereDataBase.getInstance(context).citiesDao().deleteCity(city)
            return ResponseResult.Success(city)
        }
    }

    //todo: mig to db
    fun storeCurrentSelectedCity(city: String, context: Context){

    }
//
//    fun getCurrentSelectedCity(context: Context): String {
//
//        return
//    }

}