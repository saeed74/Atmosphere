package com.arianmanesh.atmospher

import com.arianmanesh.atmospher.database.CitiesDBModel

data class WeatherItemResponse (
    val location: Location,
    val current: Current
)

data class Location (
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

data class Current (
    val last_updated: String,
    val temp_c: Long,
    val condition: Condition
)

data class Condition (
    val text: String,
    val icon: String
)