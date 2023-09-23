package com.example.mygithubuser.data.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mygithubuser.R
import com.example.mygithubuser.data.response.DetailUser
import com.example.mygithubuser.data.response.GitHubUser
import com.example.mygithubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()

        supportActionBar?.elevation = 0f

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]




        if (detailViewModel.userLogin.isEmpty()) {
            val user = intent.getParcelableExtra<GitHubUser>(EXTRA_USER) as GitHubUser
            detailViewModel.userLogin = user.login.toString()
        }

        detailViewModel.detailuser.observe(this) {
            setDetailUser(it)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar2.visibility = View.VISIBLE
            resetDetailUser()
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailUser(detailuser: DetailUser) {
        binding.textViewName.text = detailuser.login
        binding.textViewUsername.text = detailuser.name
        binding.textViewFollowers.text = detailuser.followers + " Followers"
        binding.textViewFollowing.text = detailuser.following + " Followings"

        binding.imageViewUserProfile.load(detailuser.avatarUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun resetDetailUser() {
        binding.textViewName.text = ""
        binding.textViewUsername.text = ""
        binding.textViewFollowers.text = ""
        binding.textViewFollowing.text = ""
    }

    private fun String.toUsernameFormat(): String {
        return StringBuilder("@").append(this).toString()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLE = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}
