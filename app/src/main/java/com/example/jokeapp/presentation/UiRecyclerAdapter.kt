package com.example.jokeapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokeapp.R
import com.example.jokeapp.presentation.views.CustomImageButton
import com.example.jokeapp.presentation.views.CustomTextView

class UiRecyclerAdapter<E>(
    private val listener: FavoriteItemClickListener<E>,
    private val listChanges: ListChanges<E>
) : RecyclerView.Adapter<UiRecyclerAdapter.CommonDataViewHolder<E>>() {

    fun update() {
        val result = listChanges.getDiffResult()
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonDataViewHolder<E> {
        val empty = viewType == 0
        val view = LayoutInflater.from(parent.context).inflate(
             if (empty)
                R.layout.no_favorite_item
            else
                R.layout.common_data_item,
            parent, false
        )
        return if (empty)
            CommonDataViewHolder.Failed(view)
        else
            CommonDataViewHolder.Base(view, listener)
    }

    override fun onBindViewHolder(holder: CommonDataViewHolder<E>, position: Int) {
        holder.bind(listChanges.getList()[position])
    }

    override fun getItemViewType(position: Int) = when (listChanges.getList()[position]) {
        is CommonUi.Failed -> 0
        else -> 1
    }

    override fun getItemCount(): Int = listChanges.getList().size

    abstract class CommonDataViewHolder<E>(view: View) :
        RecyclerView.ViewHolder(view) {

        private val textView = itemView.findViewById<CustomTextView>(R.id.commonDataTextView)

        open fun bind(model: CommonUi<E>) = model.show(textView)

          class Base<E>(view: View, private val listener: FavoriteItemClickListener<E>) :
            CommonDataViewHolder<E>(view) {
            private val iconView = itemView.findViewById<CustomImageButton>(R.id.favoriteButton)

            override fun bind(model: CommonUi<E>) {
                super.bind(model)
                model.show(iconView)

                iconView.setOnClickListener {
                    model.change(listener)
                }
            }
        }

        class Failed<E>(view: View) : CommonDataViewHolder<E>(view)
    }

}

interface FavoriteItemClickListener<E> {
    fun change(id: E)
}