package com.akbar.dbdpv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akbar.dbdpv1.databinding.ActivityBreedBinding

class BreedActivity : AppCompatActivity() {

    private lateinit var rvDog : RecyclerView
    private lateinit var binding: ActivityBreedBinding
    private val list = ArrayList<DogBreed>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBreedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvDog = binding.rvDogBreed
        rvDog.setHasFixedSize(true)

        list.addAll(listDogs)
        showGridView()

        supportActionBar?.hide()
    }

    private val listDogs: ArrayList<DogBreed>
        get(){
            val dataNama = resources.getStringArray(R.array.dogbreed)
            val dataFoto = resources.obtainTypedArray(R.array.dogphotos)
            val dataActivity = resources.getStringArray(R.array.activity)
            val dataBarking = resources.getStringArray(R.array.barking)
            val dataTrain = resources.getStringArray(R.array.trainability)
            val dataProtective = resources.getStringArray(R.array.protectivenature)
            val dataComHelProb = resources.getStringArray(R.array.chp)
            val listDog = ArrayList<DogBreed>()
            for (i in dataNama.indices){
                val dog = DogBreed(dataNama[i],
                    dataFoto.getResourceId(i, -1),
                    dataActivity[i],
                    dataBarking[i],
                    dataTrain[i],
                    dataProtective[i],
                    dataComHelProb[i])
                listDog.add(dog)
            }
            return listDog
        }

    private fun showRecyclerList(){
        rvDog.layoutManager = LinearLayoutManager(this)
        val listDogAdapter = GridAdapter(list)
        rvDog.adapter = listDogAdapter
    }

    private fun showGridView(){
        rvDog.layoutManager = GridLayoutManager(this, 2)
        val listDogAdapter = GridAdapter(list)
        rvDog.adapter = listDogAdapter

        listDogAdapter.setOnItemClickCallback(object : GridAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DogBreed) {
                val intentToDetail = Intent(this@BreedActivity, DetailBreedActivity::class.java)
                intentToDetail.putExtra("DATA", data)
                startActivity(intentToDetail)
            }
        })
    }
}