package com.example.dicodingsubmission2.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dicodingsubmission2.BuildConfig
import com.example.dicodingsubmission2.R
import com.example.dicodingsubmission2.api.ApiConfig
import com.example.dicodingsubmission2.databinding.ActivityUserBinding
import com.example.dicodingsubmission2.response.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    companion object {
        private const val TAG = "UserActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = intent.getStringExtra("login")

        if (login != null) {
            setUserDetail(login)
        }else {
            Log.e(TAG, "onCreate: login is null")
        }
    }

    private fun setUserDetail(login: String) {
        showLoading(true)
        ApiConfig.getApiService().getUserDetail(login).enqueue(object: Callback<UserDetailResponse> {
            override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    binding.apply {
                        tvName.text = user?.name
                        tvLogin.text = user?.login
                        tvFollowers.text = getString(R.string.follower, user?.followers)
                        tvFollowing.text = getString(R.string.following, user?.following)
                        Glide.with(this@UserActivity)
                            .load(BuildConfig.IMG_BASE_URL + user?.id + "?v=4")
                            .into(pvUser)
                    }
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
            binding.cardView.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.cardView.visibility = View.VISIBLE
        }
    }

}