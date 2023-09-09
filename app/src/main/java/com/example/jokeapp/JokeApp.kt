package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.RealmProvider
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.NewJokeService
import com.example.jokeapp.data.cloud.QuoteService
import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.domain.FailureHandler
import com.example.jokeapp.presentation.CommonViewModel
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.presentation.StateCommunication
import io.realm.DynamicRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class JokeApp : Application() {

    lateinit var jokeViewModel: CommonViewModel.Joke
    lateinit var quoteViewModel: CommonViewModel.Quote
    lateinit var jokeCommunication: StateCommunication<Int>
    lateinit var quoteCommunication: StateCommunication<String>

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configuration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://google.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val manageResources = ManageResources.Base(this)

        val realmProvider = object : RealmProvider {
            override fun provideRealm() = Realm.getDefaultInstance()
        }
        val failureHandler = FailureHandler.Factory(manageResources)
        jokeCommunication = StateCommunication.Base()
        quoteCommunication = StateCommunication.Base()

        jokeViewModel = CommonViewModel.Joke(
            CommonInteractor.Base(
                Repository.Base(
                    CloudDataSource.NewJoke(retrofit.create(NewJokeService::class.java)),
                    CacheDataSource.Joke(realmProvider)
                ),
                failureHandler
            ),
            jokeCommunication
        )
        quoteViewModel = CommonViewModel.Quote(
            CommonInteractor.Base(
                Repository.Base(
                    CloudDataSource.Quote(retrofit.create(QuoteService::class.java)),
                    CacheDataSource.Quote(realmProvider)
                ),
                failureHandler
            ),
            quoteCommunication
        )
    }
}