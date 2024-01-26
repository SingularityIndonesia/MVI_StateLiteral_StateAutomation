package com.singularityindonesia.android.mviliteralstatestateautomation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.singularityindonesia.android.mviliteralstatestateautomation.databinding.ActivityMainBinding
import com.singularityindonesia.android.mviliteralstatestateautomation.util.viewBinding

class MainActivity : FragmentActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
    }
}