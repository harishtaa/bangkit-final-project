package com.akbar.dbdpv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BookmarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        supportActionBar?.hide()
    }
}