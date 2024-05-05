package com.example.roomdatabase42024

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    suspend fun addUserData(userData: UserData) {
        userDao.addUserData(userData)
    }

    suspend fun updateUserData(userData: UserData) {
        userDao.updateUserData(userData)
    }

    suspend fun deleteUserData(userData: UserData) {
        userDao.deleteUserData(userData)
    }

    fun userLogin(email: String, password: String) = userDao.userLogin(email, password)

    fun readUserData(): LiveData<List<UserData>> = userDao.readUserData()
}