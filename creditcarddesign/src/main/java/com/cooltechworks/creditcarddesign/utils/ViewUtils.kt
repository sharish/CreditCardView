package com.cooltechworks.creditcarddesign.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard() {
    this.post {
        requestFocus()
        val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideKeyboard() {
    val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}