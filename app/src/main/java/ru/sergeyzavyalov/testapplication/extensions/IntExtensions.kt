package ru.sergeyzavyalov.testapplication.extensions

import android.content.res.Resources

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.dpFloat: Float get() = this * Resources.getSystem().displayMetrics.density