package com.jay.iconfinderassignment.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

const val TAG = "JayLogTag"
const val AUTH_TOKEN = "Bearer X0vjEUN6KRlxbp2DoUkyHeM0VOmxY91rA6BbU5j3Xu6wDodwS0McmilLPBWDUcJ1"

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q, lambda = 0)
inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}