package com.example.jokeapp.sl

import com.example.jokeapp.core.data.cache.RealmProvider
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.QuoteService
import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.domain.FailureHandler
import com.example.jokeapp.presentation.StateCommunication
import com.example.jokeapp.presentation.quote.QuotesViewModel
import retrofit2.Retrofit

class QuotesModule(
    private val failureHandler: FailureHandler,
    private val realmProvider: RealmProvider,
    private val retrofit: Retrofit
) : Module.Base<String, QuotesViewModel>() {

    private var communication: StateCommunication<String>? = null

    override fun getViewModel() = QuotesViewModel(
        CommonInteractor.Base(
            Repository.Base(
                CloudDataSource.Quote(retrofit.create(QuoteService::class.java)),
                CacheDataSource.Quote(realmProvider)
            ),
            failureHandler
        ),
        getCommunication()
    )


    override fun getCommunication(): StateCommunication<String> {
        if (communication == null) {
            communication = StateCommunication.Base()
        }
        return communication!!
    }
}