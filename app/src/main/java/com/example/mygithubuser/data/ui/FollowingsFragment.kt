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
import com.example.mygithubuser.databinding.FragmentFollowingsBinding

class FollowingsFragment : Fragment() {

    private lateinit var binding: FragmentFollowingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingsBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowings.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowings.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailViewModel.allfollowings.observe(viewLifecycleOwner) {
            setUserFollowingData(it)
        }

        detailViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
//            binding.rvFollowings.visibility = View.GONE
            binding.progressBarFollowings.visibility = View.VISIBLE
//            binding.rvFollowings.alpha = 0.0F
        } else {
            binding.progressBarFollowings.visibility = View.GONE
//            binding.rvFollowings.alpha = 1F
        }
    }

    private fun setUserFollowingData(user: List<GitHubUser>) {
        binding.rvFollowings.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollowings.apply {
            binding.rvFollowings.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = UserGitHubAdapter(user)
            binding.rvFollowings.adapter = listUserAdapter

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