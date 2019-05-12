package com.reece.linetvhomework.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Model {
    /**
     * {
     * "drama_id": 1,
     * "name": "致我們單純的小美好",
     * "total_views": 23562274,
     * "created_at": "2017-11-23T02:04:39.000Z",
     * "thumb": "https://i.pinimg.com/originals/61/d4/be/61d4be8bfc29ab2b6d5cab02f72e8e3b.jpg",
     * "rating": 4.4526
     * },
     */
    data class Drama(
        val drama_id: Int,
        val name: String,
        val total_views: Long,
        val created_at: Date,
        val thumb: String,
        val rating: Float
    ) : Parcelable {
        constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readLong(),
            source.readSerializable() as Date,
            source.readString(),
            source.readFloat()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeInt(drama_id)
            writeString(name)
            writeLong(total_views)
            writeSerializable(created_at)
            writeString(thumb)
            writeFloat(rating)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Drama> = object : Parcelable.Creator<Drama> {
                override fun createFromParcel(source: Parcel): Drama = Drama(source)
                override fun newArray(size: Int): Array<Drama?> = arrayOfNulls(size)
            }
        }
    }

    /**
     * {
     * "data": [{drama1}, {drama2}...]
     * }
     */
    data class Result(val data: ArrayList<Drama>)
}