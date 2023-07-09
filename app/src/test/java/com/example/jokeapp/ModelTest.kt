package com.example.jokeapp

import org.junit.Assert.assertEquals
import org.junit.Test


class ModelTest {

    @Test
    fun `successful return`() {

        val viewModel = ViewModel(FakeModel())
        viewModel.init(object : TextCallback {
            override fun provideText(string: String) {
                assertEquals("fake\ntext", string)
            }
        })
        viewModel.getJoke()
    }

    @Test
    fun `fail return`() {
        val model =FakeModel()
        val viewModel = ViewModel(model)
        model.isSuccess = false

        viewModel.init(object : TextCallback {
            override fun provideText(string: String) {
                assertEquals("Fake error", string)
            }
        })
        viewModel.getJoke()
    }
}


class FakeModel : Model<Joke, Error> {

    private var callback: ResultCallback<Joke, Error> = ResultCallback.Empty()
    var isSuccess = true
    override fun getJoke() {
        if (isSuccess) {
            callback.provideSuccess(Joke("fake", "text"))
        } else {
            callback.provideError(FakeError())
        }
    }


    override fun clear() {
        this.callback = ResultCallback.Empty()
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        this.callback = resultCallback
    }

}

class FakeError : Error {
    override fun message() = "Fake error"
}