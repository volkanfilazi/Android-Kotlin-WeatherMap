package com.example.projekt1rain

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

    class MyBarChartConverter : ValueFormatter() {
        private val days = arrayOf("Mo", "Tu", "Wed", "Th", "Fr", "Sa", "Su")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }

