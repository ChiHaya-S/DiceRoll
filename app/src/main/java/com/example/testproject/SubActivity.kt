package com.example.testproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import com.example.testproject.databinding.ActivitySubBinding


class SubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE)
        binding.textView.text = message

        binding.button.setOnClickListener {
            finish()
        }

    }
}