package com.example.roomdatabase42024

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserData(userData: UserData)

    @Update
    suspend fun updateUserData(userData: UserData)

    @Delete
    suspend fun deleteUserData(userData: UserData)

    @Query("SELECT * FROM UserTable ORDER BY id ASC")
    fun readUserData(): LiveData<List<UserData>>

    @Query("SELECT * FROM UserTable WHERE email LIKE:email AND password LIKE:password")
    fun userLogin(email: String, password: String): UserData
}