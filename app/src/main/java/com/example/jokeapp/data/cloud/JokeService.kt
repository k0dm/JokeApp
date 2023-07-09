package com.example.jokeapp.data.cloud


import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

interface JokeService {

    fun getJoke(serviceCallback: ServiceCallback)

    class Base : JokeService {
        override fun getJoke(serviceCallback: ServiceCallback) {
            Thread {
                var connection: HttpURLConnection? = null
                try {
                    val url = URL("https://official-joke-api.appspot.com/random_joke")
                    connection = url.openConnection() as HttpURLConnection
                    InputStreamReader(BufferedInputStream(connection.inputStream)).use {
                        val line: String = it.readText()
                        serviceCallback.returnSuccess(line)
                    }
                } catch (e: Exception) {
                    val errorType =
                        if (e is UnknownHostException) {
                            ErrorType.NO_INTERNET
                        } else {
                            ErrorType.OTHER
                        }
                    serviceCallback.returnError(errorType)
                } finally {
                    connection?.disconnect()
                }
            }.start()
        }
    }
}