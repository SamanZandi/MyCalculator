package com.zandroid.mycalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class MainViewModel :ViewModel() {

    var calculationText:String="0"
    var resultText:String="0"

fun appendText(text:String){
    if (calculationText.equals("0") || resultText.equals("0")){
      calculationText=""
      resultText=""
    }
    calculationText+=text
    resultText+=text
}

}