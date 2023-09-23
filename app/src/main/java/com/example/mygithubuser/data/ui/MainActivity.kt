package com.example.mygithubuser.data.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.data.response.GitHubUser
import com.example.mygithubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserGitHubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val items = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(items)

        adapter = UserGitHubAdapter(emptyList())
        binding.rvUsers.adapter = adapter

        mainViewModel.user.observe(this) { user ->
            setListData(user)
            showLoading(false)
        }

        mainViewModel.listUser.observe(this) { listData ->
            setListData(listData)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.getDataSearch(searchView.text.toString())
                    true
                }
        }
    }

    private fun setListData(listGitHub: List<GitHubUser>) {
        adapter.updateData(listGitHub)
        binding.rvUsers.visibility = if (listGitHub.isEmpty()) View.GONE else View.VISIBLE
        adapter.setOnItemClickCallback(object : UserGitHubAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GitHubUser) {
                selectedUser(data)
            }
        })
    }

    private fun selectedUser(data: GitHubUser) {
        val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(moveIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar1.visibility = View.VISIBLE
        } else {
            binding.progressBar1.visibility = View.GONE
        }
    }
}
