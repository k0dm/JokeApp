package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.data.Model
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.data.cloud.JokeService
import com.example.jokeapp.presentation.ViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApp : Application() {

    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        viewModel = ViewModel(
            Model.Base(
                ManageResources.Base(this),
                retrofit.create(JokeService::class.java)
            )
        )
    }
}