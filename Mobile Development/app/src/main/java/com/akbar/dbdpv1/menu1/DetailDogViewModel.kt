package com.akbar.dbdpv1.menu1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.akbar.dbdpv1.database.BookmarkDog
import com.akbar.dbdpv1.database.BookmarkDogDao
import com.akbar.dbdpv1.database.DatabaseDog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailDogViewModel(application: Application): AndroidViewModel(application) {

    private val dogDao : BookmarkDogDao?
    private val dogDb : DatabaseDog?

    init {
        dogDb = DatabaseDog.getDatabase(application)
        dogDao = dogDb.bookmarkDogDao()
    }

    fun addToBookmark(id: Int, name: String){
        CoroutineScope(Dispatchers.IO).launch {
            val dog = BookmarkDog(id, name)
            dogDao?.addToBookmark(dog)
        }
    }

    fun checkBookmark(id: Int) = dogDao?.checkDog(id)

    fun deleteBookmark(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            dogDao?.deleteDog(id)
        }
    }
}