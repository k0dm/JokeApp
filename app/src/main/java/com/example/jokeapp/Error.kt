package com.example.jokeapp

interface Error {

    fun message(): String

    class NoConnection(private val manageResources: ManageResources): Error{

        override fun message() =manageResources.getString(R.string.no_connection)
    }

    class ServiceUnavailable(private val manageResources: ManageResources): Error {

        override fun message() =manageResources.getString(R.string.service_unavailable)
    }
}