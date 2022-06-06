package com.akbar.dbdpv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akbar.dbdpv1.databinding.ActivityDetailBreedBinding

class DetailBreedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBreedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBreedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataDog = intent.getParcelableExtra<DogBreed>("DATA")

        val tvNama = binding.tvDogNameDetail
        val ivFoto = binding.ivIvDogPicDetail
        val tvAl = binding.tvResultAl
        val tvBl = binding.tvResultBl
        val tvTn = binding.tvResultTn
        val tvPn = binding.tvResultPn

        ivFoto.setImageResource(dataDog?.foto!!)
        tvNama.text = dataDog.nama
        tvAl.text = dataDog.activity
        tvBl.text = dataDog.barking
        tvTn.text = dataDog.trainability
        tvPn.text = dataDog.protective

        supportActionBar?.hide()
    }
}