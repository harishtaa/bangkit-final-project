package com.akbar.dbdpv1.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akbar.dbdpv1.BookmarkActivity
import com.akbar.dbdpv1.menu2.CameraActivity
import com.akbar.dbdpv1.databinding.ActivityMainBinding
import com.akbar.dbdpv1.menu1.BreedActivity
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
        binding.btnBookmark.setOnClickListener { Intent(this, BookmarkActivity::class.java).also { startActivity(it) }}
        binding.btnExit.setOnClickListener {
            finish()
            exitProcess(0)
        }
    }
}