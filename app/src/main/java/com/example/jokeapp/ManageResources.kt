package com.example.jokeapp

import android.content.Context
import androidx.annotation.StringRes

interface ManageResources {

    fun getString(@StringRes stringId: Int): String

    class Base(private val context: Context) : ManageResources {

        override fun getString(stringId: Int) = context.getString(stringId)
    }
}