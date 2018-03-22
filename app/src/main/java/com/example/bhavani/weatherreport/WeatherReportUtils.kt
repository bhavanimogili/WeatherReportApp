package com.example.bhavani.weatherreport

import android.app.Activity
import com.example.bhavani.weatherreport.R
import java.util.*

/**
 * Created by bhavani on 21/03/2018.
 */
class WeatherReportUtils {
    companion object {
         fun setWeatherIcon(activity: Activity, actualId: Int, sunrise: Long, sunset: Long): String {
            val id = actualId / 100
            var icon = ""
            if (actualId == 800) {
                val currentTime = Date().time
                if (currentTime >= sunrise && currentTime < sunset) {
                    icon = activity.getString(R.string.weather_sunny)
                } else {
                    icon = activity.getString(R.string.weather_clear_night)
                }
            } else {
                when (id) {
                    2 -> icon = activity.getString(R.string.weather_thunder)
                    3 -> icon = activity.getString(R.string.weather_drizzle)
                    7 -> icon = activity.getString(R.string.weather_foggy)
                    8 -> icon = activity.getString(R.string.weather_cloudy)
                    6 -> icon = activity.getString(R.string.weather_snowy)
                    5 -> icon = activity.getString(R.string.weather_rainy)
                }
            }
            return icon
        }
    }
}