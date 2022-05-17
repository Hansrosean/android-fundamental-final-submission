package com.latihan.android.githubuserapp3.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * from User Order By username ASC")
    fun getAllUsers(): LiveData<List<User>>
}