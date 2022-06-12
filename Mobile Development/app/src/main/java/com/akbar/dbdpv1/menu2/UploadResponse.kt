package com.akbar.dbdpv1.menu2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UploadResponse(

	@field:SerializedName("dogdata:")
	val dogdata: Dogdata
)

@Parcelize
data class Dogdata(

	@field:SerializedName("activity")
	val activity: String,

	@field:SerializedName("chp")
	val chp: String,

	@field:SerializedName("barking")
	val barking: String,

	@field:SerializedName("protec")
	val protec: String,

	@field:SerializedName("breed")
	val breed: String,

	@field:SerializedName("pict")
	val pict: String,

	@field:SerializedName("train")
	val train: String
): Parcelable
