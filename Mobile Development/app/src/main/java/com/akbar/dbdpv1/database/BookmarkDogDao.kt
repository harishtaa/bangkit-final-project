package com.akbar.dbdpv1.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDogDao {
    @Insert
    fun addToBookmark (bookmarkDog: BookmarkDog)

    @Query("SELECT * FROM dog_bookmark")
    fun getDogBookmark() : LiveData<List<BookmarkDog>>

    @Query("SELECT count(*) FROM dog_bookmark WHERE dog_bookmark.id = :id")
    fun checkDog(id: Int): Int

    @Query("DELETE FROM dog_bookmark WHERE dog_bookmark.id = :id")
    fun deleteDog(id: Int): Int
}