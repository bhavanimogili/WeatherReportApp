package com.example.bhavani.weatherreport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val weatherReportFragment = WeatherReportFragment()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, weatherReportFragment).commit()
        }
    }
}
