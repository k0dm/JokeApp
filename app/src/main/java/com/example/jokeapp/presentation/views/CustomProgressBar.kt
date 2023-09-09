package com.example.jokeapp.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.example.jokeapp.core.ShowProgressBar

class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr), ShowProgressBar {

    override fun show(arg: Boolean) {
        visibility = if (arg) View.VISIBLE else View.GONE
    }
}