package com.example.jokeapp.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.example.jokeapp.core.ShowImageView

class CustomImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr), ShowImageView {

    override fun show(arg: Int) {
        setImageResource(arg)
    }
}