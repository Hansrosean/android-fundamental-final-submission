package com.latihan.android.githubuserapp3.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.latihan.android.githubuserapp3.room.User
import com.latihan.android.githubuserapp3.room.UserDao
import com.latihan.android.githubuserapp3.room.UserDatabase
import com.latihan.android.githubuserapp3.room.UserRepository

class DetailViewModel(application: Application) : ViewModel()  {
    private val mUserRepository: UserRepository = UserRepository(application)

    private var userDao: UserDao
    private var userDb: UserDatabase = UserDatabase.getDatabase(application)

    init {
        userDao = userDb.userDao()
    }
    fun insert(user: User) {
        mUserRepository.insert(user)
    }
    fun delete(user: User) {
        mUserRepository.delete(user)
    }

}