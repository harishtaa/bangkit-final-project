package com.akbar.dbdpv1.menu1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.akbar.dbdpv1.DogBreed
import com.akbar.dbdpv1.databinding.ActivityDetailBreedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailBreedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBreedBinding
    private lateinit var viewModel: DetailDogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBreedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailDogViewModel::class.java]

        val dataDog = intent.getParcelableExtra<DogBreed>("DATA")

        val id = dataDog?.id
        val tvNama = binding.tvDogNameDetail
        val ivFoto = binding.ivDogPic
        val tvAl = binding.tvResultAl
        val tvBl = binding.tvResultBl
        val tvTn = binding.tvResultTn
        val tvPn = binding.tvResultPn
        val tvCHP = binding.tvIsiChp

        ivFoto.setImageResource(dataDog?.foto!!)
        tvNama.text = dataDog.nama
        tvAl.text = dataDog.activity
        tvBl.text = dataDog.barking
        tvTn.text = dataDog.trainability
        tvPn.text = dataDog.protective
        tvCHP.text = dataDog.comhelprob

        supportActionBar?.hide()

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = id?.let { viewModel.checkBookmark(it) }
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0 ){
                        binding.togbutFavorite.isChecked = true
                        isChecked = true
                    }else{
                        binding.togbutFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.togbutFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked){
                if (id != null){
                    viewModel.addToBookmark(id, tvNama.toString())
                }
                Toast.makeText(this, "Dog added to bookmarks", Toast.LENGTH_SHORT).show()
            }else{
                if (id != null) {
                    viewModel.deleteBookmark(id)
                }
                Toast.makeText(this, "Dog deleted from bookmarks", Toast.LENGTH_SHORT).show()
            }
        }
    }
}