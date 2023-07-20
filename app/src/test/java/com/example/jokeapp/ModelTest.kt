package com.example.jokeapp

import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ResultCallback
import com.example.jokeapp.presentation.Error
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.DataCallback
import com.example.jokeapp.presentation.ViewModel
import org.junit.Assert.assertEquals
import org.junit.Test


//class ModelTest {
//
//    @Test
//    fun `successful return`() {
//
//        val viewModel = ViewModel(FakeRepository())
//        viewModel.init(object : DataCallback {
//            override fun provideText(string: String) {
//                assertEquals("fake\ntext", string)
//            }
//        })
//        viewModel.getJoke()
//    }
//
//    @Test
//    fun `fail return`() {
//        val model =FakeRepository()
//        val viewModel = ViewModel(model)
//        model.isSuccess = false
//
//        viewModel.init(object : DataCallback {
//            override fun provideText(string: String) {
//                assertEquals("Fake error", string)
//            }
//        })
//        viewModel.getJoke()
//    }
//}
//
//
//class FakeRepository : Repository<JokeUi, Error> {
//
//    private var callback: ResultCallback<JokeUi, Error> = ResultCallback.Empty()
//    var isSuccess = true
//    override fun getJoke() {
//        if (isSuccess) {
//            callback.provideSuccess(JokeUi("fake", "text"))
//        } else {
//            callback.provideError(FakeError())
//        }
//    }
//
//
//    override fun clear() {
//        this.callback = ResultCallback.Empty()
//    }
//
//    override fun init(resultCallback: ResultCallback<JokeUi, Error>) {
//        this.callback = resultCallback
//    }
//
//}
//
//class FakeError : Error {
//    override fun message() = "Fake error"
}