package com.dualtrack.app.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun modeToString(mode: Mode): String = mode.name

    @TypeConverter
    fun stringToMode(value: String): Mode = Mode.from(value)
}
