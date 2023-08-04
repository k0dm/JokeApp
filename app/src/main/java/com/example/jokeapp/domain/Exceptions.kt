package com.example.jokeapp.domain

import io.realm.internal.IOException

class NoConnectionException: IOException()

class ServiceUnavailableException: IOException()

class NoCachedException: IOException()