package com.example.jokeapp.sl

import androidx.lifecycle.ViewModel
import com.example.jokeapp.presentation.CommonViewModel
import com.example.jokeapp.presentation.StateCommunication

interface Module<T : ViewModel> {

    fun getViewModel(): T

    abstract class Base<E, T : CommonViewModel.Abstract<E>> : Module<T> {

        abstract fun getCommunication(): StateCommunication<E>
    }
}
