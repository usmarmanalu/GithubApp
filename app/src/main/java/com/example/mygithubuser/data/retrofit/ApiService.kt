package com.example.mygithubuser.data.retrofit

import com.example.mygithubuser.data.response.DetailUser
import com.example.mygithubuser.data.response.GitHubResponse
import com.example.mygithubuser.data.response.GitHubUser
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("users")
    fun getUsers(
        @Header("Authorization") token: String
    ): Call<List<GitHubUser>>

    @GET("search/users")
    fun getUserSearch(
        @Query("q") login: String,
        @Header("Authorization") token: String
    ): Call<GitHubResponse>

    @GET("users/{login}")
    fun getDetailUser(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): Call<DetailUser>

    @GET("users/{login}/followers")
    fun getFollowers(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<GitHubUser>>

    @GET("users/{login}/following")
    fun getFollowings(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<GitHubUser>>
}

