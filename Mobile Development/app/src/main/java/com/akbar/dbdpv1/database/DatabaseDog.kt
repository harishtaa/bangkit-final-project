package com.akbar.dbdpv1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookmarkDog::class], version = 1)
abstract class DatabaseDog: RoomDatabase() {

    abstract fun bookmarkDogDao() : BookmarkDogDao

    companion object{
        @Volatile
        private var INSTANCE :DatabaseDog? = null

        @JvmStatic
        fun getDatabase(context: Context): DatabaseDog{
            if (INSTANCE == null){
                synchronized(DatabaseDog::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    DatabaseDog::class.java, "database_dog")
                        .build()
                }
            }
            return INSTANCE as DatabaseDog
        }
    }
}