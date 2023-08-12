package com.example.myapplication.utils

import android.content.Context
import android.os.Handler
import android.widget.Toast

class ToastUtils {
    companion object {
        private var isToastShowing = false
        fun showToast(message: String, cxt: Context, duration: Int = Toast.LENGTH_SHORT) {
            if (!isToastShowing) {
                val toast = Toast.makeText(cxt, message, duration)
                toast.show()
                isToastShowing = true
                Handler().postDelayed({
                    isToastShowing = false
                }, 2500L)
            }
        }
    }
}