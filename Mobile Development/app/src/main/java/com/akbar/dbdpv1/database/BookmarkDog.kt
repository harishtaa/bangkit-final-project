package com.akbar.dbdpv1.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "dog_bookmark")
@Parcelize
data class BookmarkDog(
    val name: Int,

    @PrimaryKey
    val id: String
) : Parcelable