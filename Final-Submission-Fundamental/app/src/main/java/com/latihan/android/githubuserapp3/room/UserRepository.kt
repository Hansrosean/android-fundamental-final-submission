package com.latihan.android.githubuserapp3.room

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    
    private val mUsersDao : UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    
    init {
        val db = UserDatabase.getDatabase(application)
        mUsersDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<User>> = mUsersDao.getAllUsers()

    fun insert(user: User){
        executorService.execute{mUsersDao.insert(user)}
    }

    fun delete(user: User){
        executorService.execute{mUsersDao.delete(user)}
    }
}