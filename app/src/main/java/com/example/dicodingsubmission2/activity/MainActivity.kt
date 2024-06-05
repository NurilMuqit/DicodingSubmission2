package com.example.dicodingsubmission2.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmission2.adapter.UserAdapter
import com.example.dicodingsubmission2.api.ApiConfig
import com.example.dicodingsubmission2.databinding.ActivityMainBinding
import com.example.dicodingsubmission2.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            searchView.setupWithSearchBar(searchBar)
        }

        supportActionBar?.hide()

        val layoutManager= LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        setUserView()
    }

    private fun setUserView() {
        showLoading(true)
        ApiConfig.getApiService().getSearchUsers("1").enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val listUser = response.body()?.items
                    val adapter = UserAdapter()
                    adapter.submitList(listUser)
                    binding.rvUser.adapter = adapter
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

}