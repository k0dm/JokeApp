package com.example.jokeapp.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr), ShowImageView {

    override fun show(arg: Int) {
        setImageResource(arg)
    }
}