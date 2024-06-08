package com.zandroid.mycalculator.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zandroid.mycalculator.utils.TABLE_HISTORY


@Entity(tableName = TABLE_HISTORY)
data class CalcEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val expression:String="",
    val result: String="",
)
