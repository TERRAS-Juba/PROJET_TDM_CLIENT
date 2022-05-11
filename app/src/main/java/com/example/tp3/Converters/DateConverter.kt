package com.example.tp3.Converters

import androidx.room.TypeConverter
import java.sql.Timestamp
import java.util.*

class DateConverter {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }
        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time?.toLong()
        }
}