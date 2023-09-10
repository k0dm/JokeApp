package com.example.jokeapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jokeapp.R
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        show(JokesFragment())
        val tabChosen: (Boolean) -> Unit = { jokesChosen ->
            if (jokesChosen) {
                show(JokesFragment())
            } else {
                show(QuoteFragment())
            }
        }
        tabLayout.addOnTabSelectedListener(TabListener(tabChosen))
    }

    private fun  show(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}