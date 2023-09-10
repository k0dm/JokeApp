package com.example.jokeapp.presentation

interface ProvideStateCommunication<E> {

    fun provide(): StateCommunication<E>

     class Base<E> : ProvideStateCommunication<E> {

        private val communication = StateCommunication.Base<E>()

        override fun provide() = communication
    }
}