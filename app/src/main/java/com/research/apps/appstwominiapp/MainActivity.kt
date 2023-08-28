package com.research.apps.appstwominiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.research.apps.appstwominiapp.databinding.ActivityMainTwoBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

        val message = "hello word of the affirmation"
        val capsMessage = message.capitalizeEachWord()
        binding.tvHello.text = capsMessage
    }
}