package com.example.datetimepickerapp

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