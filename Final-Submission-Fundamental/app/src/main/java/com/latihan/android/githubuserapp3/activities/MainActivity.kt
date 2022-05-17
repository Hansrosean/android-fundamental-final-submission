package com.latihan.android.githubuserapp3.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.latihan.android.githubuserapp3.BuildConfig
import com.latihan.android.githubuserapp3.R
import com.latihan.android.githubuserapp3.room.User
import com.latihan.android.githubuserapp3.adapters.ListUserAdapter
import com.latihan.android.githubuserapp3.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    
    val listUser  = ArrayList<User>()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        
        binding?.rvUser?.setHasFixedSize(true)
        
        getListUser()
    }
    
    
    // search bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                binding?.progressBar?.visibility = View.VISIBLE
                val client = AsyncHttpClient()
                val url = "https://api.github.com/search/users?q=$query"
                client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
                client.addHeader("User-Agent", "request")
                client.get(url, object : AsyncHttpResponseHandler() {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>,
                        responseBody: ByteArray
                    ) {
                        listUser.clear()
                        binding?.progressBar?.visibility = View.INVISIBLE
                        val result = String(responseBody)
                        Log.d(TAG, result)
                        try {
                            val jsonObject = JSONObject(result)
                            val jsonArray = jsonObject.getJSONArray("items")
                            for (i in 0 until jsonArray.length()) {
                                val item = jsonArray.getJSONObject(i)
                                val username = item.getString("login")
                                val id = item.getString("id")
                                val avatar = item.getString("avatar_url")

                                val user = User(username, id, avatar)
                                listUser.add(user)
                            }
                            val listUserAdapter = ListUserAdapter(listUser)
                            listUserAdapter.notifyDataSetChanged()
                            binding?.rvUser?.adapter = listUserAdapter

                            val layoutManager = LinearLayoutManager(this@MainActivity)
                            binding?.rvUser?.layoutManager = layoutManager

                            val itemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
                            binding?.rvUser?.addItemDecoration(itemDecoration)

                            listUserAdapter.setOnItemClickCallBack(object :
                                ListUserAdapter.OnItemClickCallBack {
                                override fun onItemClicked(data: User) {
                                    showSelectedUser(data)
                                }
                            })
                            
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
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
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                })
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting_bar -> {
                val moveToSettingsActivityIntent = Intent(this, SettingActivity::class.java)
                startActivity(moveToSettingsActivityIntent)
            }
            R.id.favorite_bar -> {
                val moveToFavoriteActivity = Intent(this, FavoriteActivity::class.java)
                startActivity(moveToFavoriteActivity)
            }
        }
        return true
    }

    // menampilkan data user
    private fun getListUser() {
        binding?.progressBar?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            // jika koneksi berhasil
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                binding?.progressBar?.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val id = jsonObject.getString("id")

                        val user = User(username, id, avatar)
                        listUser.add(user)
                    }

                    val listUserAdapter = ListUserAdapter(listUser)
                    binding?.rvUser?.adapter = listUserAdapter

                    val layoutManager = LinearLayoutManager(this@MainActivity)
                    binding?.rvUser?.layoutManager = layoutManager

                    val itemDecoration =
                        DividerItemDecoration(this@MainActivity, layoutManager.orientation)
                    binding?.rvUser?.addItemDecoration(itemDecoration)

                    listUserAdapter.setOnItemClickCallBack(object :
                        ListUserAdapter.OnItemClickCallBack {
                        override fun onItemClicked(data: User) {
                            showSelectedUser(data)
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            // jika koneksi gagal
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
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val moveToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        moveToDetail.putExtra(DetailActivity.EXTRA_DATA, user)

        startActivity(moveToDetail)
    }
}