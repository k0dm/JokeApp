package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.core.ManageResources
import com.example.jokeapp.core.data.cache.PersistentDataSource
import com.example.jokeapp.core.data.cache.RealmProvider
import com.example.jokeapp.domain.FailureHandler
import com.example.jokeapp.sl.JokesModule
import com.example.jokeapp.sl.MainModule
import com.example.jokeapp.sl.QuotesModule
import com.example.jokeapp.sl.ViewModelsFactory
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class JokeApp : Application() {

    private lateinit var failureHandler: FailureHandler
    private lateinit var realmProvider: RealmProvider
    private lateinit var retrofit: Retrofit

    val viewModelsFactory by lazy {
        ViewModelsFactory(
            MainModule(PersistentDataSource.Base(this)),
            JokesModule(failureHandler, realmProvider, retrofit),
            QuotesModule(failureHandler, realmProvider, retrofit)
        )
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configuration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(configuration)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://google.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        realmProvider = object : RealmProvider {
            override fun provideRealm() = Realm.getDefaultInstance()
        }

        failureHandler = FailureHandler.Factory(ManageResources.Base(this))
    }
}