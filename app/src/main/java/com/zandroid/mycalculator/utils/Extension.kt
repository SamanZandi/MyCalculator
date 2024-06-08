package com.zandroid.mycalculator.utils

import android.view.View

fun View.showVisibility(isShown: Boolean) {
    if (isShown) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}