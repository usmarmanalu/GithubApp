package com.example.mygithubuser.data.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mygithubuser.data.response.GitHubUser
import com.example.mygithubuser.databinding.ListUserBinding

class UserGitHubAdapter(private var listUsers: List<GitHubUser>) :
    ListAdapter<GitHubUser, UserGitHubAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var onItemCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemCallback = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<GitHubUser>) {
        listUsers = newData
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GitHubUser) {
            binding.tvUserGitHub.text = "${user.login}"
            binding.ivUserGitHub.load(user.avatarUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUsers[position]
        holder.bind(user)

        holder.itemView.setOnClickListener {
            onItemCallback?.onItemClicked(user)
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: GitHubUser)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GitHubUser>() {
            override fun areItemsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
                return oldItem == newItem
            }

        }
    }
}



