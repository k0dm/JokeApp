package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var jokeViewModel: CommonViewModel.Joke
    private lateinit var quoteViewModel: CommonViewModel.Quote
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jokeViewModel = (application as JokeApp).jokeViewModel
        quoteViewModel = (application as JokeApp).quoteViewModel

        val jokeFavoriteDataView = findViewById<FavoriteDataView>(R.id.jokeFavoriteDataView)
        val quoteFavoriteDataView = findViewById<FavoriteDataView>(R.id.quoteFavoriteDataView)
        jokeFavoriteDataView.linkWith(jokeViewModel)
        quoteFavoriteDataView.linkWith(quoteViewModel)

        jokeViewModel.observe(this){state->
            jokeFavoriteDataView.show(state)
        }
        quoteViewModel.observe(this){state->
            quoteFavoriteDataView.show(state)
        }
    }
}