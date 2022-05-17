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
import com.latihan.android.githubuserapp3.databinding.FragmentFollowerBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerFragment : Fragment() {
    
    private var _binding : FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    
    val listUser = ArrayList<User>()
    
    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getListFollower()
        
        binding.rvFollower.setHasFixedSize(true)
    }

    private fun getListFollower() {
        val username = arguments?.getString(DetailActivity.EXTRA_DATA)
        
        binding.progressBarFollower.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                binding.progressBarFollower.visibility = View.INVISIBLE
                
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val followerUsername = jsonObject.getString("login")
                        val followerId = jsonObject.getString("id")
                        val followerPhoto = jsonObject.getString("avatar_url")
                        
                        val user = User(followerUsername, followerId, followerPhoto)
                        listUser.add(user)
                    }
                    
                    val listUserAdapter = ListUserAdapter(listUser)
                    binding.rvFollower.adapter = listUserAdapter
                    
                    val layoutManager = LinearLayoutManager(activity)
                    binding.rvFollower.layoutManager = layoutManager
                    
                    val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
                    binding.rvFollower.addItemDecoration(itemDecoration)

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
                binding.progressBarFollower.visibility = View.INVISIBLE
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