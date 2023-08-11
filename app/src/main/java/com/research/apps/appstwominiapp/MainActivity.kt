package com.research.apps.appstwominiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initView()
    }

    private fun initView() {
        val tvHello = findViewById<TextView>(R.id.tv_hello)

        val message = "hello word of the affirmation"
        val capsMessage = message.capitalizeEachWord()
        tvHello.text = capsMessage
    }
}