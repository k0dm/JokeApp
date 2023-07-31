/*
package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.jokeapp.data.DispatcherList
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var repository: FakeRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var toFavoriteMapper: FakeMapper
    private lateinit var toBaseMapper: FakeMapper
    private lateinit var jokeUiCallback: FakeJokeUiCallback
    private lateinit var communication: FakeCommunication

    @Before
    fun setUp() {
        repository = FakeRepository()
        toFavoriteMapper = FakeMapper(true)
        toBaseMapper = FakeMapper(false)
        jokeUiCallback = FakeJokeUiCallback()
        communication = FakeCommunication()
        viewModel = MainViewModel(
            communication,
            repository,
            toBaseMapper,
            toFavoriteMapper,
            FakeDispatchers()
        )
    }

    @Test
    fun test_successful_not_favorite() {
        repository.returnFetchJokeResult = FakeJokeResult(
            FakeJoke(10, "fakeText", "fakePunchline", "type"),
            isFavorite = false,
            isSuccessful = true,
            errorMessage = "testErrorMessage"
        )
        viewModel.getJoke()
        val expected = FakeJokeUi(10,"fakeText", "fakePunchline", false)
        assertEquals(expected, communication.data)
    }

    @Test
    fun test_successful_is_favorite() {
        repository.returnFetchJokeResult = FakeJokeResult(
            FakeJoke(10, "fakeText", "fakePunchline", "type"),
            isFavorite = true,
            isSuccessful = true,
            errorMessage = "testErrorMessage"
        )
        viewModel.getJoke()
        val expected = FakeJokeUi(10,"fakeText", "fakePunchline",  true)
        assertEquals(expected, communication.data)
    }

    @Test
    fun test_not_successful() {
        repository.returnFetchJokeResult = FakeJokeResult(
            FakeJoke(10, "fakeText", "fakePunchline", "type"),
            isFavorite = true,
            isSuccessful = false,
            errorMessage = "testErrorMessage"
        )
        viewModel.getJoke()
        val expected = JokeUi.Failed("testErrorMessage")
        assertEquals(expected, communication.data)
    }

    @Test
    fun test_joke_status() {
        repository.returnChangeJokeStatus = FakeJokeUi(99, "fakeText", "fakePunchline", false)
        viewModel.changeJokeStatus()
        val expected = FakeJokeUi(99,"fakeText", "fakePunchline",  false)
        assertEquals(expected, communication.data)
    }
}

private class FakeHandleUi(private val dispatchers: DispatcherList) : HandleUi {
    override fun handle(
        coroutineScope: CoroutineScope,
        jokeUiCallback: JokeUiCallback,
        block: suspend () -> JokeUi
    ) {
        coroutineScope.launch(dispatchers.io()) {
            block.invoke().show(jokeUiCallback)
        }
    }

}

private class FakeDispatchers : DispatcherList {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = TestCoroutineDispatcher()

    override fun io() = dispatcher
    override fun ui() = dispatcher
}

private class FakeJokeUiCallback : JokeUiCallback {
    val provideTextList = mutableListOf<String>()
    override fun provideText(text: String) {
        provideTextList.add(text)
    }

    val provideIconResIdList = mutableListOf<Int>()
    override fun provideResId(resId: Int) {
        provideIconResIdList.add(resId)
    }
}

private class FakeMapper(var toFavorite: Boolean) : Joke.Mapper<JokeUi> {

    override fun map(id: Int, text: String, punchLine: String, type: String): JokeUi {
        return FakeJokeUi(id, text, punchLine, toFavorite)
    }
}

private data class FakeJoke(
    private val id: Int,
    private val text: String,
    private val punchLine: String,
    private val type: String
) : Joke {
    override fun <T> map(mapper: Joke.Mapper<T>): T {
        return mapper.map(id, text, punchLine, type)
    }
}

private data class FakeJokeUi(
    val id: Int,
    val text: String,
    val punchLine: String,
    val isFavorite: Boolean
) : JokeUi {
    override fun show(jokeUiCallback: JokeUiCallback) = with(jokeUiCallback) {
        provideText(text + "_" + punchLine)
        provideResId(if (isFavorite) id + 1 else id)
    }
}

private data class FakeJokeResult(
    private val joke: Joke,
    private val isFavorite: Boolean,
    private val isSuccessful: Boolean,
    private val errorMessage: String
) : JokeResult {
    override fun isFavorite() = isFavorite

    override fun isSuccessful() = isSuccessful

    override fun errorMessage() = errorMessage

    override fun <T> map(mapper: Joke.Mapper<T>): T = joke.map(mapper)
}

private class FakeRepository : Repository {
    var returnFetchJokeResult: JokeResult? = null
    override suspend fun fetch(): JokeResult {
        return returnFetchJokeResult!!
    }

    override fun chooseFavorite(fromCache: Boolean) {
        TODO("Not yet implemented")
    }

    var returnChangeJokeStatus: JokeUi? = null
    override suspend fun changeJokeStatus(): JokeUi {
        return returnChangeJokeStatus!!
    }
}

private class FakeCommunication : JokeCommunication {
    lateinit var data: JokeUi
    override fun map(data: JokeUi) {
        this.data = data
    }
}*/
