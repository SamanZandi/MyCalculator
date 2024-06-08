package com.zandroid.mycalculator.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CalcDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calcEntity: CalcEntity)

    @Delete
    suspend fun deleteExpression(calcEntity: CalcEntity)

    @Query("DELETE FROM table_history")
    suspend fun clearHistory()



    @Query("SELECT * FROM table_history ORDER BY id DESC")
    fun getAllHistory():MutableList<CalcEntity>
}