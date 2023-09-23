package com.example.mygithubuser.data.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.data.response.DetailUser
import com.example.mygithubuser.data.response.GitHubUser
import com.example.mygithubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val _detailuser = MutableLiveData<DetailUser>()
    val detailuser: LiveData<DetailUser> = _detailuser

    private val _allfollowers = MutableLiveData<ArrayList<GitHubUser>>()
    val allfollowers: LiveData<ArrayList<GitHubUser>> = _allfollowers

    private val _allfollowings = MutableLiveData<ArrayList<GitHubUser>>()
    val allfollowings: LiveData<ArrayList<GitHubUser>> = _allfollowings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    var userLogin: String = ""
        set(value) {
            field = value
            getDetailUser()
            getDetailUserFollowers()
            getDetailUserFollowings()
        }

    private fun getDetailUser() {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getDetailUser(userLogin, ApiConfig.APICode)
        api.enqueue(object : Callback<DetailUser> {
            override fun onResponse(
                call: Call<DetailUser>,
                response: Response<DetailUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailuser.value = responseBody
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getDetailUserFollowers() {
        _isLoadingFollower.value = true
        val api = ApiConfig.getApiService().getFollowers(userLogin, ApiConfig.APICode)
        api.enqueue(object : Callback<ArrayList<GitHubUser>> {
            override fun onResponse(
                call: Call<ArrayList<GitHubUser>>,
                response: Response<ArrayList<GitHubUser>>
            ) {
                _isLoadingFollower.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _allfollowers.value = responseBody as ArrayList<GitHubUser>?
                    Log.d(TAG, "Followers response: $responseBody")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<GitHubUser>>, t: Throwable) {
                _isLoadingFollower.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getDetailUserFollowings() {
        _isLoadingFollowing.value = true
        val api = ApiConfig.getApiService().getFollowings(userLogin, ApiConfig.APICode)
        api.enqueue(object : Callback<ArrayList<GitHubUser>> {
            override fun onResponse(
                call: Call<ArrayList<GitHubUser>>,
                response: Response<ArrayList<GitHubUser>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _allfollowings.value = responseBody as ArrayList<GitHubUser>?
                    Log.d(TAG, "Following response: $responseBody")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<GitHubUser>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}

