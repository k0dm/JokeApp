package com.example.jokeapp.presentation

import androidx.recyclerview.widget.DiffUtil

class CommonDiffUtilCallback<E>(
    private val oldList: List<CommonUi<E>>,
    private val newList: List<CommonUi<E>>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].same(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = false

}