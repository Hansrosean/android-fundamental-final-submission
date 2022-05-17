package com.latihan.android.githubuserapp3.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavoriteViewModel private constructor(private val mApplication: Application): ViewModelProvider.NewInstanceFactory() {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(ListFavoriteViewModel::class.java)){
            return ListFavoriteViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown viewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModel? = null
        @JvmStatic
        fun getInstance(application: Application): FavoriteViewModel {
            if (INSTANCE == null) {
                synchronized(SettingModelFactory::class.java) {
                    INSTANCE = FavoriteViewModel(application)
                }
            }
            return INSTANCE as FavoriteViewModel
        }
    }
}