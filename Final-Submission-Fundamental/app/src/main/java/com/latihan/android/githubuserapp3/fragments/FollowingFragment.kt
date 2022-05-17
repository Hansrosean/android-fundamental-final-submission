package com.latihan.android.githubuserapp3.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.android.githubuserapp3.BuildConfig
import com.latihan.android.githubuserapp3.room.User
import com.latihan.android.githubuserapp3.activities.DetailActivity
import com.latihan.android.githubuserapp3.adapters.ListUserAdapter
import com.latihan.android.githubuserapp3.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingFragment : Fragment() {
    
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    
    val listUser = ArrayList<User>()
    
    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getListFollowing()
        
        binding.rvFollowing.setHasFixedSize(true)
    }

    private fun getListFollowing() {
        val username = arguments?.getString(DetailActivity.EXTRA_DATA)
        
        binding.progressBarFollowing.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                binding.progressBarFollowing.visibility = View.INVISIBLE
                
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val followingUsername = jsonObject.getString("login")
                        val followingId = jsonObject.getString("id")
                        val followingPhoto = jsonObject.getString("avatar_url")

                        val user = User(followingUsername, followingId, followingPhoto)
                        listUser.add(user)
                    }
                        val listUserAdapter = ListUserAdapter(listUser)
                        binding.rvFollowing.adapter = listUserAdapter

                        val layoutManager = LinearLayoutManager(activity)
                        binding.rvFollowing.layoutManager = layoutManager

                        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
                        binding.rvFollowing.addItemDecoration(itemDecoration)

                        listUserAdapter.setOnItemClickCallBack(object :
                            ListUserAdapter.OnItemClickCallBack {
                            override fun onItemClicked(data: User) {
                                showSelectedUser(data)
                            }
                        })
                    
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBarFollowing.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }) 
    }

    private fun showSelectedUser(user: User) {
        val moveToDetail = Intent(requireActivity(), DetailActivity::class.java)
        moveToDetail.putExtra(DetailActivity.EXTRA_DATA, user)

        startActivity(moveToDetail)
    }
}