package com.singularityindonesia.android.mviliteralstatestateautomation.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by : Stefanus Ayudha Junior
 * Mailto : stefanus.ayudha@gmail.com
 * Created at : Friday 1/26/24 : 10:10 AM
 **/

fun <T> Fragment.collect(
    state: Flow<T>,
    collector: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope
        .launch {
            state.collect {
                collector(it)
            }
        }
}