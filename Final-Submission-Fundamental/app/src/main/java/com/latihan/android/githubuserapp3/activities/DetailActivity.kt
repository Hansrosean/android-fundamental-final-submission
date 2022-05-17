package com.latihan.android.githubuserapp3.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.latihan.android.githubuserapp3.BuildConfig
import com.latihan.android.githubuserapp3.R
import com.latihan.android.githubuserapp3.room.User
import com.latihan.android.githubuserapp3.adapters.SectionPagerAdapter
import com.latihan.android.githubuserapp3.databinding.ActivityDetailBinding
import com.latihan.android.githubuserapp3.viewmodel.DetailViewModel
import com.latihan.android.githubuserapp3.viewmodel.FavoriteViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    private var user: User? = null
    private lateinit var detailViewModel: DetailViewModel
    
    companion object {
        const val EXTRA_DATA = "extra_data"
        private val TAG = DetailActivity::class.java.simpleName

        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        
        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        getDetailUser()
        
        val dataUser = intent.getParcelableExtra<User>(EXTRA_DATA) as User
        val username = dataUser.username
        
        val bundle = Bundle()
        bundle.putString(EXTRA_DATA, username)
        
        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        
        binding?.apply { 
            viewPager2.adapter = sectionPagerAdapter
            TabLayoutMediator(tabs, viewPager2) { tab, position ->
                tab.text = resources.getString(TAB_TITLE[position])
            }.attach()
        }
        
        detailViewModel = obtainViewModel(this@DetailActivity)

        user = intent.getParcelableExtra(EXTRA_DATA)

        user.let { user ->
            user?.username = dataUser.username
            user?.id = dataUser.id
            user?.avatar = dataUser.avatar
        }
        
        binding?.fabFavorite?.setOnClickListener {
            try {
                detailViewModel.insert(user as User)
                showToast(resources.getString(R.string.added_favorite))
            } catch (e: java.lang.Exception) {
                showToast("Error : ${e.message}")
            }
        }

        binding?.fabDelete?.setOnClickListener {
            try {
                detailViewModel.delete(user as User)
                showToast(resources.getString(R.string.delete_favorite))
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = FavoriteViewModel.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun getDetailUser() {
        binding?.progressBar?.visibility = View.VISIBLE
        
        val dataUser = intent.getParcelableExtra<User>(EXTRA_DATA) as User
        val userName = dataUser.username
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$userName"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                binding?.progressBar?.visibility = View.INVISIBLE
                
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val name = responseObject.getString("name")
                    val username = responseObject.getString("login")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val avatar = responseObject.getString("avatar_url")
                    val repository = responseObject.getString("public_repos")
                    binding?.apply {
                        Glide.with(this@DetailActivity)
                            .load(avatar)
                            .centerCrop()
                            .into(imgItemPhoto)
                        tvCompany.text = company
                        tvLocation.text = location
                        tvName.text = name
                        tvUsername.text = username
                        tvRepository.text = repository
                }
            } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                } 
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding?.progressBar?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}