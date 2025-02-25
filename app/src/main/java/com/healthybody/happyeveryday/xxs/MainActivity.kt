package com.healthybody.happyeveryday.xxs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.healthybody.happyeveryday.xxs.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onBackPressed() { }

    override fun onResume() {
        super.onResume()

        load()
    }

    var job: Job?=null
    fun load(){
        lifecycleScope.launch(Dispatchers.Main) {
            flow<Int> {
                delay(1200)
                emit(100)
            }.flowOn(Dispatchers.IO).collect(){
                startActivity(Intent(this@MainActivity,HomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}