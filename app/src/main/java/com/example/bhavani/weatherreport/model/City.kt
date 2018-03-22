package com.example.bhavani.weatherreport.model

import android.app.Activity
import android.content.SharedPreferences

/**
 * Created by bhavani on 21/03/2018.
 */
class City(activity: Activity) {

    var preferences: SharedPreferences

    init {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE)
    }

     var city: String
        get() = preferences.getString("city", "London,UK")
        set(city) {
            preferences.edit().putString("city", city).commit()
        }
}
