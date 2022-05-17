package com.latihan.android.githubuserapp3.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.android.githubuserapp3.R
import com.latihan.android.githubuserapp3.adapters.ListFavoriteAdapter
import com.latihan.android.githubuserapp3.databinding.ActivityFavoriteBinding
import com.latihan.android.githubuserapp3.viewmodel.FavoriteViewModel
import com.latihan.android.githubuserapp3.viewmodel.ListFavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding

    private lateinit var adapter: ListFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.favorite_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        
        binding?.tvNoFavorite?.visibility = View.INVISIBLE

        val listFavViewModel = obtainViewModel(this@FavoriteActivity)
        listFavViewModel.getAllUsers().observe(this) { userList ->
            adapter.setListUsers(userList)
            if (userList.isNotEmpty()) {
                binding?.tvNoFavorite?.visibility = View.INVISIBLE
            } else {
                binding?.tvNoFavorite?.visibility = View.VISIBLE
            }

        }

        adapter = ListFavoriteAdapter()

        binding?.rvUserFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserFavorite?.setHasFixedSize(true)
        binding?.rvUserFavorite?.adapter = adapter

    }

    private fun obtainViewModel(activity: AppCompatActivity): ListFavoriteViewModel {
        val factory = FavoriteViewModel.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ListFavoriteViewModel::class.java]
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }
}