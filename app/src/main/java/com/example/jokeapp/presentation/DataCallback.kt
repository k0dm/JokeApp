package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes

interface DataCallback {

    fun provideText(string: String)

    fun provideResId(@DrawableRes resId: Int)

    class Empty : DataCallback {

        override fun provideText(string: String) = Unit
        override fun provideResId(resId: Int) = Unit
    }
}