package com.example.bhavani.weatherreport

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bhavani.weatherreport.model.City

import org.json.JSONObject

import java.text.DateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by bhavani on 21/03/2018.
 */
class WeatherReportFragment : Fragment() {
    // Can refactor the fragment to MVP
    internal lateinit var weatherFont: Typeface
    internal var handler: Handler

    private lateinit var cityField: TextView
    private lateinit var updatedField: TextView
    private lateinit var detailsField: TextView
    private lateinit var currentTemperatureField: TextView
    private lateinit var weatherIcon: TextView

    init {
        handler = Handler()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_weather_report, container, false)
        cityField = rootView?.findViewById(R.id.city_field) as TextView
        updatedField = rootView?.findViewById(R.id.updated_field) as TextView
        detailsField = rootView?.findViewById(R.id.details_field) as TextView
        currentTemperatureField = rootView?.findViewById(R.id.current_temperature_field) as TextView
        weatherIcon = rootView?.findViewById(R.id.weather_icon) as TextView

        weatherIcon.setTypeface(weatherFont);
        var showWeatherButton = rootView?.findViewById(R.id.show_weather_button);
        var enterCityField = rootView?.findViewById(R.id.enter_city_field) as EditText
        showWeatherButton?.setOnClickListener {
            val city = City(activity)
            city.city = enterCityField.text.toString()
            updateWeatherData(city.city)
        }
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherFont = Typeface.createFromAsset(activity.assets, "fonts/weather.ttf")
        updateWeatherData(City(activity).city)
    }

    private fun updateWeatherData(city: String) {
        //Can also use rx java service here using subscribers - time constraints :(
        Thread() {
            run {
                val json = WeatherServiceProvider.getJSON(activity, city)
                if (json == null) {
                    handler.post {
                        Toast.makeText(activity,
                                activity.getString(R.string.place_not_found),
                                Toast.LENGTH_LONG).show()
                    }
                } else {
                    handler.post { renderWeather(json) }
                }
            }
        }.start()
    }

    // Can refactor this better..Time constraints :(
    private fun renderWeather(json: JSONObject) {
        try {
            cityField.text = json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country")

            val details = json.getJSONArray("weather").getJSONObject(0)
            val main = json.getJSONObject("main")
            detailsField.text = details.getString("description").toUpperCase(Locale.US) +
                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
                    "\n" + "Pressure: " + main.getString("pressure") + " hPa"

            currentTemperatureField.text = String.format("%.2f", main.getDouble("temp")) + " ℃"

            val df = DateFormat.getDateTimeInstance()
            val updatedOn = df.format(Date(json.getLong("dt") * 1000))
            updatedField.text = "Last update: " + updatedOn

            WeatherReportUtils.Companion.setWeatherIcon(activity, details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000)

        } catch (e: Exception) {
            Log.e("JSON", "error")
        }
    }
}
