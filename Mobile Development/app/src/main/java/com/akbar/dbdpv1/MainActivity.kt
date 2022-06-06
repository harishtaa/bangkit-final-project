package com.akbar.dbdpv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akbar.dbdpv1.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBreed.setOnClickListener { Intent(this, BreedActivity::class.java).also { startActivity(it) }}
        binding.btnCamera.setOnClickListener { Intent(this, CameraActivity::class.java).also { startActivity(it) }}
        binding.btnBookmark.setOnClickListener { Intent(this, CameraActivity::class.java).also { startActivity(it) }}
        binding.btnExit.setOnClickListener {
            finish()
            exitProcess(0)
        }
    }
}