package com.example.mygithubuser.data.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.data.response.GitHubResponse
import com.example.mygithubuser.data.response.GitHubUser
import com.example.mygithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _user = MutableLiveData<List<GitHubUser>>(emptyList())
    val user: LiveData<List<GitHubUser>> = _user

    private val _listUser = MutableLiveData<List<GitHubUser>>()
    val listUser: LiveData<List<GitHubUser>> = _listUser

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getData()
    }

    companion object {
        private const val TAG = "MainActifity"
    }

    fun replaceUserList() {
        _listUser.value = _user.value
    }

    fun getDataSearch(searchUser: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserSearch(searchUser, MainActivity.APICode)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listUser.value = response.body()?.items as List<GitHubUser>?
                    Log.d(TAG, "Data diterima: ${_listUser.value?.size} item")
                } else {
                    Log.e(TAG, "OnFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    private fun getData() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(MainActivity.APICode)
        client.enqueue(object : Callback<List<GitHubUser>> {
            override fun onResponse(
                call: Call<List<GitHubUser>>,
                response: Response<List<GitHubUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _user.value = responseBody
                } else {
                    Log.e(TAG, "OnFailure : ${response.message()} " + response.code())
                }
            }

            override fun onFailure(call: Call<List<GitHubUser>>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}" + t.message)
            }
        })
    }
}