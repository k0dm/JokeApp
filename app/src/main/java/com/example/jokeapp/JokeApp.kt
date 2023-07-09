package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.data.cloud.JokeService

class JokeApp : Application() {

    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()

        viewModel = ViewModel(Model.Base(ManageResources.Base(this), JokeService.Base()))
    }
}