package com.latihan.android.githubuserapp3.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.latihan.android.githubuserapp3.R
import com.latihan.android.githubuserapp3.preferences.SettingPreferences
import com.latihan.android.githubuserapp3.viewmodel.SettingViewModel
import com.latihan.android.githubuserapp3.viewmodel.SettingModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val splashScreen = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(splashScreen)
            finish()
        }, 3000)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            SettingModelFactory(pref)
        )[SettingViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }    
    }
}