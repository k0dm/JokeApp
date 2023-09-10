package com.example.jokeapp.presentation

import androidx.recyclerview.widget.DiffUtil.DiffResult

interface ListChanges<E>: ListProvider<E> {

    fun getDiffResult(): DiffResult

}