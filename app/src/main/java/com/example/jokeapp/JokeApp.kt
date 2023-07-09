package com.example.jokeapp

import android.app.Application

class JokeApp : Application() {

    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()

        viewModel = ViewModel(Model.Test(ManageResources.Base(this)))
    }
}