package com.example.mygithubuser.data.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.data.response.GitHubUser
import com.example.mygithubuser.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailViewModel.allfollowers.observe(viewLifecycleOwner) {
            setUserFollowerData(it)
        }

        detailViewModel.isLoadingFollower.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.rvFollowers.visibility = View.GONE
            binding.progressBarFollowers.visibility = View.VISIBLE
            binding.rvFollowers.alpha = 0.0F
        } else {
            binding.progressBarFollowers.visibility = View.GONE
            binding.rvFollowers.alpha = 1F
        }
    }

    private fun setUserFollowerData(user: List<GitHubUser>) {
        binding.rvFollowers.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollowers.apply {
            binding.rvFollowers.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = UserGitHubAdapter(user)
            binding.rvFollowers.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : UserGitHubAdapter.OnItemClickCallback {
                override fun onItemClicked(data: GitHubUser) {
                    val detailViewModel =
                        ViewModelProvider(requireActivity())[DetailViewModel::class.java]
                    detailViewModel.userLogin = data.login.toString()
                }
            })
        }
    }
}