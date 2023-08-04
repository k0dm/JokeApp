package com.example.jokeapp.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr), ShowButton {

    override fun show(arg: Boolean) {
        isEnabled = arg
    }
}