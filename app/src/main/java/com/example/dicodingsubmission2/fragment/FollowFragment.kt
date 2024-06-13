package com.example.dicodingsubmission2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmission2.activity.UserActivity
import com.example.dicodingsubmission2.adapter.UserAdapter
import com.example.dicodingsubmission2.api.ApiConfig
import com.example.dicodingsubmission2.databinding.FragmentFollowBinding
import com.example.dicodingsubmission2.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var adapter: UserAdapter

    companion object{
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
    private var position: Int = 0
    private var username: String? = null
    private var users: List<ItemsItem> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        rvFollow()
        fetchData()
    }

    private fun rvFollow() {
        binding.rvFollow.layoutManager = LinearLayoutManager(context)
        binding.rvFollow.setHasFixedSize(true)
        adapter = UserAdapter()
        binding.rvFollow.adapter = adapter
    }

    private fun fetchData() {
        username?.let {
            val client = if (position == 1) {
                ApiConfig.getApiService().getUserFollowers(it)
            } else {
                ApiConfig.getApiService().getUserFollowing(it)
            }
            showLoading(true)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                    if (response.isSuccessful) {
                        rvFollow()
                        fetchData()
                        users = response.body()?: listOf()
                        adapter.submitList(users)

                        showLoading(false)
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    Log.e(UserActivity.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}