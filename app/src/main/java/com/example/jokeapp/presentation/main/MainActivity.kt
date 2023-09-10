package com.example.jokeapp.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R
import com.example.jokeapp.presentation.TabListener
import com.example.jokeapp.presentation.joke.JokesFragment
import com.example.jokeapp.presentation.quote.QuoteFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModel = ViewModelProvider(
            this,
            (application as JokeApp).viewModelsFactory
        ).get(MainViewModel::class.java)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val tabChosen: (Int) -> Unit = { position ->
            mainViewModel.save(position)
        }
        tabLayout.addOnTabSelectedListener(TabListener(tabChosen))

        mainViewModel.observe(this) { positon ->
            tabLayout.getTabAt(positon)?.select()
            val screen =
                if (positon == 0) {
                JokesFragment::class.java
            } else {
                QuoteFragment::class.java
            }
            show(screen.newInstance())
        }

        mainViewModel.init()
    }

    private fun show(fragment: Fragment) {
//        if (supportFragmentManager.fragments.isEmpty() ||
//            supportFragmentManager.fragments.last().tag != fragment.tag
//        ) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }

}