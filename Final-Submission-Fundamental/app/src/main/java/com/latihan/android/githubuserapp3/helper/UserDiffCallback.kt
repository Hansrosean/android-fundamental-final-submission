package com.latihan.android.githubuserapp3.helper

import androidx.recyclerview.widget.DiffUtil
import com.latihan.android.githubuserapp3.room.User

class UserDiffCallback(
    private val mOldUserList: List<User>,
    private val mNewUserList: List<User>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldUserList[oldItemPosition]
        val newEmployee = mNewUserList[newItemPosition]
        return oldEmployee.username == newEmployee.username && oldEmployee.avatar == newEmployee.avatar
    }
}