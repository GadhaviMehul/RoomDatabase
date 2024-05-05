package com.example.roomdatabase42024

import android.os.Parcelable
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "UserTable")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val gender: String,
    val birthDate: LocalDate = LocalDate.now(),
    val image: String
) :
    Parcelable