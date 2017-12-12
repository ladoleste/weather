package com.thevacationplanner.dto

import android.os.Parcel
import android.os.Parcelable

/**
 *Created by Anderson on 09/12/2017.
 */
data class Weather(val id: Int, var name: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<Weather> = object : Parcelable.Creator<Weather> {
            override fun createFromParcel(source: Parcel): Weather = Weather(source)
            override fun newArray(size: Int): Array<Weather?> = arrayOfNulls(size)
        }
    }
}