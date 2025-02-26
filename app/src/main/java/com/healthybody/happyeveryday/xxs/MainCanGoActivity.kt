package com.healthybody.happyeveryday.xxs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainCanGoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainOneActivity::class.java)
        startActivity(intent)
    }

}