package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var jokeViewModel: CommonViewModel.Joke
    private lateinit var quoteViewModel: CommonViewModel.Quote
    private lateinit var adapter: UiRecyclerAdapter<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jokeViewModel = (application as JokeApp).jokeViewModel
        quoteViewModel = (application as JokeApp).quoteViewModel
        val jokeFavoriteDataView = findViewById<FavoriteDataView<Int>>(R.id.jokeFavoriteDataView)
        val quoteFavoriteDataView = findViewById<FavoriteDataView<String>>(R.id.quoteFavoriteDataView)
        val jokeCommunication = (application as JokeApp).jokeCommunication

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
         adapter = UiRecyclerAdapter<Int>(object : FavoriteItemClickListener<Int>{
            override fun change(id: Int) {
                Snackbar.make(
                    jokeFavoriteDataView,
                    R.string.remove_from_favorites,
                    Snackbar.LENGTH_SHORT
                ).setAction(R.string.yes) {
                    jokeViewModel.changeItemStatus(id)
                  // val position = jokeCommunication.removeItem(id)
                  //  adapter.update(Pair(false, position))
                }.show()
            }
        }, jokeCommunication)
        recyclerView.adapter = adapter


        jokeFavoriteDataView.linkWith(jokeViewModel)

        quoteFavoriteDataView.linkWith(quoteViewModel)

        jokeViewModel.observe(this) { state ->
            jokeFavoriteDataView.show(state)
        }

        jokeViewModel.observeList(this) {
            adapter.update()
        }

        quoteViewModel.observe(this) { state ->
            quoteFavoriteDataView.show(state)
        }
    }
}