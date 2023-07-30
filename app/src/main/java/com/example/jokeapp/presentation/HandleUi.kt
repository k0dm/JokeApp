package com.example.jokeapp.presentation

import androidx.lifecycle.viewModelScope
import com.example.jokeapp.data.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface HandleUi {

    fun handle(
        coroutineScope: CoroutineScope,
        jokeUiCallback: JokeUiCallback,
        block: suspend () -> JokeUi
    )


    class Base(private val dispatcherList: DispatcherList) : HandleUi {
        override fun handle(
            coroutineScope: CoroutineScope,
            jokeUiCallback: JokeUiCallback,
            block: suspend () -> JokeUi
        ) {
            coroutineScope.launch(dispatcherList.io()) {
                val jokeUi = block.invoke()
                withContext(dispatcherList.ui()) {
                    jokeUi.show(jokeUiCallback)
                }
            }
        }


    }
}