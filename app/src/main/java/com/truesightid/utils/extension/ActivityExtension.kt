package com.truesightid.utils.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment



fun <T> Context.intentActivity(activity: Class<T>, value: String, name: String = "extra") {
    val i = Intent(applicationContext, activity)
    i.putExtra(name, value)
    startActivity(i)
}

fun <T> Context.intentActivity(activity: Class<T>) {
    val i = Intent(applicationContext, activity)
    startActivity(i)
}

fun <T> Fragment.intentActivity(activity: Class<T>, value: String, name: String = "extra") {
    val i = Intent(requireActivity(), activity)
    i.putExtra(name, value)
    startActivity(i)
}

fun <T> Fragment.intentActivity(targetClass: Class<T>) {
    startActivity(Intent(requireContext(), targetClass))
}

fun <T> Context.pushActivity(activity: Class<T>, value: String, name: String = "extra") {
    val i = Intent(applicationContext, activity)
    i.putExtra(name, value)
    i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
}

fun <T> Fragment.pushActivity(activity: Class<T>, value: String, name: String = "extra") {
    val i = Intent(requireActivity(), activity)
    i.putExtra(name, value)
    i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
}

fun <T> Context.pushActivity(activity: Class<T>) {
    val i = Intent(applicationContext, activity)
    i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
}

fun <T> Fragment.pushActivity(activity: Class<T>) {
    val i = Intent(requireActivity(), activity)
    i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
}

fun Activity.getStringExtra(name: String = "extra"): String? {
    return intent.getStringExtra(name)
}
