package com.akbar.dbdpv1

import android.os.Parcel
import android.os.Parcelable

data class DogBreed(
    var nama: String?,
    var foto: Int?,
    var id: Int?,
    var activity: String?,
    var barking: String?,
    var trainability: String?,
    var protective: String?,
    var comhelprob: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama)
        parcel.writeValue(foto)
        parcel.writeValue(id)
        parcel.writeString(activity)
        parcel.writeString(barking)
        parcel.writeString(trainability)
        parcel.writeString(protective)
        parcel.writeString(comhelprob)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DogBreed> {
        override fun createFromParcel(parcel: Parcel): DogBreed {
            return DogBreed(parcel)
        }

        override fun newArray(size: Int): Array<DogBreed?> {
            return arrayOfNulls(size)
        }
    }
}
