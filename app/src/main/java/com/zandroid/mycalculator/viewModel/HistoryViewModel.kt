package com.zandroid.mycalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zandroid.mycalculator.repository.HistoryRepository
import com.zandroid.mycalculator.room.CalcEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: HistoryRepository):ViewModel() {

    val historyList=MutableLiveData<List<CalcEntity>>()
    val emptyList:MutableLiveData<List<CalcEntity>> = MutableLiveData(emptyList())


    fun insertHistory(historyItem:CalcEntity)=viewModelScope.launch {
        repository.insertCalculation(historyItem)

    }

    fun loadHistory()=viewModelScope.launch {
        historyList.postValue(repository.getAllHistories())
    }

    fun clearHistory()=viewModelScope.launch {
        repository.clearHistory()
        historyList.value= emptyList()

    }


}