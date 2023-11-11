package com.example.datetimepickerapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
fun Int.toOrdinal(): String {
    if (this in 11..13) {
        return "$this" + "th"
    }

    return when (this % 10) {
        1 -> "$this" + "st"
        2 -> "$this" + "nd"
        3 -> "$this" + "rd"
        else -> "$this" + "th"
    }
}

@Composable
fun getScreenWidth(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}

@Composable
fun getScreenHeight(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}
