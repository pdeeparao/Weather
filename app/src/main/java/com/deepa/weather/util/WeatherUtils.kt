package com.deepa.weather.util

import java.util.Formatter
import java.util.Locale

class WeatherUtils {
    companion object {

        fun formatString(format: String, vararg objects: String): String {
            val sb = StringBuilder()
            val formatter: Formatter = Formatter(sb, Locale.US)
            return formatter.format(format, objects).toString()

        }
    }
}