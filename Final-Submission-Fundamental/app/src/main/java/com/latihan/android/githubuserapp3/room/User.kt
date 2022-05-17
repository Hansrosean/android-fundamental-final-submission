package com.latihan.android.githubuserapp3.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String,
    
    @ColumnInfo(name = "id")
    var id : String,
    
    @ColumnInfo(name = "avatar")
    var avatar: String
): Parcelable
