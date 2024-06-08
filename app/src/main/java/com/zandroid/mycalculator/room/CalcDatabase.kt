package com.zandroid.mycalculator.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalcEntity::class], version = 1, exportSchema = false)

abstract class CalcDatabase:RoomDatabase() {
    abstract fun calcDao():CalcDao

}