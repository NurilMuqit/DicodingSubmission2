package com.example.dicodingsubmission2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingsubmission2.BuildConfig
import com.example.dicodingsubmission2.activity.UserActivity
import com.example.dicodingsubmission2.databinding.ItemRowUserBinding
import com.example.dicodingsubmission2.response.ItemsItem

class UserAdapter: ListAdapter<ItemsItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, UserActivity::class.java)
            intent.putExtra("login", getItem(position).login)
            it.context.startActivity(intent)
        }
    }

    class UserViewHolder(private val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.apply {
                tvName.text = user.login
                Glide.with(itemView)
                    .load(BuildConfig.IMG_BASE_URL + user.id + "?v=4")
                    .into(pvUser)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}