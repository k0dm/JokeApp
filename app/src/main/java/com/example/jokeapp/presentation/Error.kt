package com.example.jokeapp.presentation

import androidx.annotation.StringRes
import com.example.jokeapp.R
import com.example.jokeapp.core.ManageResources

interface Error {

    fun message(): String

    abstract class Abstract(
        private val manageResources: ManageResources,
        @StringRes private val messageId: Int
    ) : Error {
        override fun message() = manageResources.getString(messageId)
    }

    class NoConnection(manageResources: ManageResources) :
        Abstract(manageResources, R.string.no_connection)

    class ServiceUnavailable(manageResources: ManageResources) :
        Abstract(manageResources, R.string.service_unavailable)

    class NoCachedItem(manageResources: ManageResources) :
            Abstract(manageResources, R.string.no_cached_item)

    class GenericError(manageResources: ManageResources):
            Abstract(manageResources, R.string.generic_error)
}