package com.example.jokeapp.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.example.jokeapp.R
import com.example.jokeapp.presentation.views.CustomButton
import com.example.jokeapp.presentation.views.CustomImageButton
import com.example.jokeapp.presentation.views.CustomProgressBar
import com.example.jokeapp.presentation.views.CustomTextView

class FavoriteDataView<E> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var checkBox: CheckBox
    private var actionButton: CustomButton
    private val favoriteButton: CustomImageButton
    private val textView: CustomTextView
    private val progressBar: CustomProgressBar

    init {
        orientation = VERTICAL

        (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.favorite_data_view, this, true)
        checkBox = getChildAt(0) as CheckBox
        val linear = getChildAt(1) as LinearLayout
        textView = linear.findViewById(R.id.textView)
        favoriteButton = linear.findViewById(R.id.favoriteButton)
        progressBar = getChildAt(2) as CustomProgressBar
        actionButton = getChildAt(3) as CustomButton

        context.theme.obtainStyledAttributes(attrs, R.styleable.FavoriteDataView, 0, 0).apply {
            try {
                val actionButtonText = getString(R.styleable.FavoriteDataView_actionButtonText)
                val checkBoxText = getString(R.styleable.FavoriteDataView_checkBoxText)
                checkBox.text = checkBoxText
                actionButton.text = actionButtonText
            } finally {
                recycle()
            }
        }
    }

    fun linkWith(commonViewModel: CommonViewModel<E>) {

            listenChanges { isChecked ->
                commonViewModel.chooseFavorites(isChecked)
            }

            handleFavoriteButton {
                commonViewModel.changeItemStatus()
              //  commonViewModel.getItemList()
            }

            handleActionButton {
                commonViewModel.getItem()
             //   commonViewModel.getItemList()
            }

        commonViewModel.getItemList()
    }

    private fun listenChanges(block: (checked: Boolean) -> Unit) =
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            block.invoke(isChecked)
        }

    private fun handleFavoriteButton(block: () -> Unit) =
        favoriteButton.setOnClickListener {
            block.invoke()
        }

    private fun handleActionButton(block: () -> Unit) =
        actionButton.setOnClickListener {
            block.invoke()
        }

    fun show(state: State) {
        state.show(progressBar, actionButton, textView, favoriteButton)
    }

    fun checkBoxText(@StringRes id: Int) = checkBox.setText(id)

    fun actionButtonText(@StringRes id: Int) = actionButton.setText(id)
}