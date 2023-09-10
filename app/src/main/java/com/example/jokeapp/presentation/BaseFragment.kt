package com.example.jokeapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<E>: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.data_fragment, container, false)
    }

    protected abstract fun getViewModel(app: JokeApp): CommonViewModel<E>

    protected abstract fun getCommunication(app: JokeApp): StateCommunication<E>

    @StringRes
    protected abstract fun checkBoxText(): Int

    @StringRes
    protected abstract fun actionButtonText(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireActivity().application as JokeApp

        val viewModel = getViewModel(application)
        val communication = getCommunication(application)

        val favoriteDataView = view.findViewById<FavoriteDataView<E>>(R.id.favoriteDataView)
        favoriteDataView.checkBoxText(checkBoxText())
        favoriteDataView.actionButtonText(actionButtonText())
        favoriteDataView.linkWith(viewModel)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = UiRecyclerAdapter(object : FavoriteItemClickListener<E>{
            override fun change(id: E) {
                Snackbar.make(
                    favoriteDataView,
                    R.string.remove_from_favorites,
                    Snackbar.LENGTH_SHORT
                ).setAction(R.string.yes) {
                    viewModel.changeItemStatus(id)
                }.show()
            }
        }, communication)
        recyclerView.adapter = adapter

        viewModel.observe(this) { state ->
            favoriteDataView.show(state)
        }

        viewModel.observeList(this) {
            adapter.update()
        }
    }
}