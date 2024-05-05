package com.example.roomdatabase42024

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel() : ViewModel() {
    private val readUserData: LiveData<List<UserData>>
    private var repository: UserRepository

    init {
        val userDao = UserDatabase.getUserDatabase().userDao()
        repository = UserRepository(userDao)
        readUserData = repository.readUserData()
    }

    fun addUserData(userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserData(userData)
        }
    }

    fun updateUserData(userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserData(userData)
        }
    }

    fun deleteUserData(userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUserData(userData)
        }
    }

    fun userLogin(email: String, password: String): MutableLiveData<UserData> {
        val observer = MutableLiveData<UserData>()
        viewModelScope.launch(Dispatchers.IO) {
            observer.postValue(repository.userLogin(email, password))
        }
        return observer
    }


}