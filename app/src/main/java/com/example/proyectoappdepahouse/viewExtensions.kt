package com.example.proyectoappdepahouse

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideSoftKeyboard(context: Context, view: View) {
    val inputMethodManager = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager

    view.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val activity = context as Activity
            activity.currentFocus?.let {
                inputMethodManager.hideSoftInputFromWindow(
                    it.windowToken, 0
                )
            }
        }
        false
    }
}