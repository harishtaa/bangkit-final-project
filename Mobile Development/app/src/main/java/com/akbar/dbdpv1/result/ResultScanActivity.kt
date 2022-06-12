package com.akbar.dbdpv1.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.akbar.dbdpv1.databinding.ActivityResultScanBinding
import com.akbar.dbdpv1.menu2.Dogdata
import com.bumptech.glide.Glide

class ResultScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Dogdata>("EXTRA_DATA")

        showLoading(true)
        if(data != null){
            binding.apply {
                supportActionBar?.hide()
                tvResultBreed.text = data.breed
                tvResultAl.text = data.activity
                tvResultBl.text = data.barking
                tvResultTn.text = data.train
                tvResultPn.text = data.protec
                tvIsiChp.text = data.chp
                Glide.with(this@ResultScanActivity)
                    .load(data.pict)
                    .centerCrop()
                    .into(civFotoAnjing)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}