package com.singularityindonesia.android.mviliteralstatestateautomation.util

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by : Stefanus Ayudha Junior
 * Mailto : stefanus.ayudha@gmail.com
 * Created at : Friday 1/26/24 : 9:16 AM
 **/

inline fun <reified VB : ViewBinding> Activity.viewBinding(): Lazy<VB> = lazy {
    val inflateMethod = VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    inflateMethod.invoke(null, layoutInflater, null, false) as VB
}

inline fun <reified VB : ViewBinding> Fragment.viewBinding(): Lazy<VB> = lazy {
    val inflateMethod = VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    inflateMethod.invoke(null, layoutInflater, parentFragment?.view?.rootView, false) as VB
}