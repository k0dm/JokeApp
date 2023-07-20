package com.example.jokeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = (application as JokeApp).viewModel
        val button = findViewById<Button>(R.id.actionButton)
        val favoriteButton = findViewById<ImageButton>(R.id.favoriteButton)
        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        val checkBox = findViewById<CheckBox>(R.id.checkbox)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.chooseFavorite(isChecked)
        }


        button.setOnClickListener {
            button.isEnabled = false
            progressBar.visibility = View.VISIBLE
            viewModel.getJoke()
        }
        favoriteButton.setOnClickListener {
            viewModel.changeJokeStatus()
        }

        viewModel.init(object : DataCallback {
            override fun provideText(text: String) = runOnUiThread {
                progressBar.visibility = View.GONE
                button.isEnabled = true
                textView.text = text
            }

            override fun provideResId(resId: Int) = runOnUiThread{
                favoriteButton.setImageResource(resId)
            }
        })
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }
}