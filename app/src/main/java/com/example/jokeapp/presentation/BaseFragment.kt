package com.example.jokeapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.jokeapp.JokeApp
import com.example.jokeapp.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<T : CommonViewModel.Abstract<E>, E> : Fragment() {

    private lateinit var viewModel: CommonViewModel<E>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(
            requireActivity(),
            (requireActivity().application as JokeApp).viewModelsFactory
        )

        viewModel = viewModelProvider.get(getViewModelClass())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("k0dm", "onCreatedView ${javaClass.simpleName}")
        return inflater.inflate(R.layout.data_fragment, container, false)
    }

    protected abstract fun getViewModelClass(): Class<T>

    @StringRes
    protected abstract fun checkBoxText(): Int

    @StringRes
    protected abstract fun actionButtonText(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteDataView = view.findViewById<FavoriteDataView<E>>(R.id.favoriteDataView)
        favoriteDataView.checkBoxText(checkBoxText())
        favoriteDataView.actionButtonText(actionButtonText())
        favoriteDataView.linkWith(viewModel)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = UiRecyclerAdapter(
            object : FavoriteItemClickListener<E> {
                override fun change(id: E) {
                    Snackbar.make(
                        favoriteDataView,
                        R.string.remove_from_favorites,
                        Snackbar.LENGTH_SHORT
                    ).setAction(R.string.yes) {
                        viewModel.changeItemStatus(id)
                    }.show()
                }
            }, viewModel
        )
        recyclerView.adapter = adapter

        viewModel.observe(this) { state ->
            favoriteDataView.show(state)
        }

        viewModel.observeList(this) {
            adapter.update()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("k0dm", "onDestroyView ${javaClass.simpleName}")
    }
}