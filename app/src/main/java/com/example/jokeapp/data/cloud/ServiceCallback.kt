package com.example.jokeapp.data.cloud

interface ServiceCallback {

    fun returnSuccess(data: String)

    fun returnError(type: ErrorType)
}