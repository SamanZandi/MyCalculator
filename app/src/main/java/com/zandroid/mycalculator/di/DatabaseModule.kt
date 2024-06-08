package com.zandroid.mycalculator.di

import android.content.Context
import androidx.room.Room
import com.zandroid.mycalculator.room.CalcDatabase
import com.zandroid.mycalculator.room.CalcEntity
import com.zandroid.mycalculator.utils.DATABASE_HISTORY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)=Room.databaseBuilder(
        context,CalcDatabase::class.java, DATABASE_HISTORY)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db:CalcDatabase)=db.calcDao()


    @Provides
    @Singleton
    fun provideEntity()=CalcEntity()

}