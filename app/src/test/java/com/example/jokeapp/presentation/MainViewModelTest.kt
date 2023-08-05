package com.example.jokeapp.presentation


import com.example.jokeapp.core.DispatcherList
import com.example.jokeapp.core.Joke
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.data.JokeDataModel
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.cache.ChangeJokeStatus
import com.example.jokeapp.domain.Communication
import com.example.jokeapp.domain.JokeDomain
import com.example.jokeapp.domain.JokeFailureHandler
import com.example.jokeapp.domain.JokeInteractor
import com.example.jokeapp.domain.StateCommunication
import com.example.jokeapp.views.ShowButton
import com.example.jokeapp.views.ShowImageView
import com.example.jokeapp.views.ShowProgressBar
import com.example.jokeapp.views.ShowText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FakeRepository
    private lateinit var interactor: JokeInteractor
    private lateinit var toFavoriteMapper: FakeMapper
    private lateinit var toBaseMapper: FakeMapper
    private lateinit var communication: FakeCommunication

    @Before
    fun setUp() {
        repository = FakeRepository()
        interactor = FakeInteractor(repository, FakeJokeFailureHandler())
        toFavoriteMapper = FakeMapper(true)
        toBaseMapper = FakeMapper(false)
        communication = FakeCommunication()
        viewModel = MainViewModel(
            interactor,
            communication,
            FakeState("", 0, false),
            toBaseMapper,
            toFavoriteMapper,
            FakeDispatchers()
        )
    }

    @Test
    fun test_successful_not_favorite() {
        repository.returnFetchJokeResult =
            FakeDataModel(10, "fakeText", "fakePunchline", "type", false)
        viewModel.getJoke()
        val expected = FakeState("fakeText\nfakePunchline", 10, false)
        assertEquals(expected, communication.data[1])
    }

    @Test
    fun test_successful_is_favorite() {
        repository.returnFetchJokeResult =
            FakeDataModel(10, "fakeText", "fakePunchline", "type", true)
        viewModel.getJoke()
        val expected = FakeState("fakeText\nfakePunchline", 10, true)
        assertEquals(expected, communication.data[1])
    }

    @Test
    fun test_not_successful() {
        repository.isSuccessful = false

        viewModel.getJoke()
        val expected = State.Failed("fake_error", 0)
        assertEquals(expected, communication.data[1])
    }

    @Test
    fun test_joke_status() {
        repository.returnFetchJokeResult =
            FakeDataModel(11, "fakeText", "fakePunchline", "type", true)
        repository.returnChangeJokeStatus =
            FakeDataModel(11, "fakeText", "fakePunchline", "type", false)
        viewModel.getJoke()
        val expectedFetch = FakeState("fakeText\nfakePunchline", 11, true)
        assertEquals(expectedFetch, communication.data[1])
        viewModel.changeJokeStatus()
        val expectedChange = FakeState("fakeText\nfakePunchline", 11, false)
        assertEquals(expectedChange, communication.data[2])
    }

    private data class FakeState(val text: String, val id: Int, val favorites: Boolean) : State {
        override fun show(
            progress: ShowProgressBar,
            button: ShowButton,
            textView: ShowText,
            imageButton: ShowImageView
        ) {
            TODO("Not yet implemented")
        }

        override fun show(progress: ShowProgressBar, button: ShowButton) {
            TODO("Not yet implemented")
        }

        override fun show(textView: ShowText, imageButton: ShowImageView) {
            TODO("Not yet implemented")
        }
    }

    private class FakeMapper(var toFavorite: Boolean) : Mapper<JokeUi> {

        override fun map(id: Int, text: String, punchLine: String, type: String): JokeUi {
            return FakeJokeUi(id, text, punchLine, toFavorite)
        }
    }

    private data class FakeJokeUi(
        val id: Int,
        val text: String,
        val punchLine: String,
        val isFavorite: Boolean
    ) : JokeUi {

        override fun show(communication: Communication<State>) {
            communication.map(FakeState("$text\n$punchLine", id, isFavorite))
        }
    }

    private class FakeInteractor(
        private val repository: FakeRepository,
        private val jokeFailureHandler: JokeFailureHandler
    ) : JokeInteractor {
        override suspend fun getJoke(): JokeDomain {
            return try {
                val resultJoke = repository.fetch()
                FakeJokeDomain.Success(resultJoke)
            } catch (e: Exception) {
                FakeJokeDomain.Fail(jokeFailureHandler.handle(e))
            }
        }

        override suspend fun changeJokeStatus(): JokeDomain {

            val resultJoke = repository.changeJokeStatus()
            return FakeJokeDomain.Success(resultJoke)

        }

        override fun getFavoriteJokes(favorites: Boolean) {
            repository.chooseFavorite(favorites)
        }

    }

    private class FakeJokeFailureHandler : JokeFailureHandler {
        override fun handle(e: Exception): Error = FakeError()
    }

    private class FakeError : Error {
        override fun message() = "fake_error"
    }

    private class FakeDispatchers : DispatcherList {

        @OptIn(ExperimentalCoroutinesApi::class)
        private val dispatcher = TestCoroutineDispatcher()

        override fun io() = dispatcher
        override fun ui() = dispatcher
    }

    private interface FakeJokeDomain : JokeDomain {
        data class Success(
            private val joke: JokeDataModel,
        ) : FakeJokeDomain {
            override fun isFavorite() = joke.isFavorite()

            override fun isSuccessful() = true

            override fun errorMessage() = ""

            override fun <T> map(mapper: Mapper<T>): T = joke.map(mapper)
        }

        data class Fail(private val error: Error) : FakeJokeDomain {
            override fun isSuccessful(): Boolean = false

            override fun errorMessage(): String = error.message()

            override fun <T> map(mapper: Mapper<T>): T = throw IllegalStateException()

            override fun isFavorite(): Boolean = false
        }
    }

    private data class FakeDataModel(
        private val id: Int,
        private val text: String,
        private val punchline: String,
        private val type: String,
        private val isFavorite: Boolean = false
    ) : JokeDataModel {

        override fun <T> map(mapper: Mapper<T>): T = mapper.map(id, text, punchline, type)

        override suspend fun change(changeJokeStatus: ChangeJokeStatus): JokeDataModel =
            changeJokeStatus.addOrRemove(id, this)


        override fun isFavorite(): Boolean = isFavorite

    }

    private class FakeRepository : Repository {
        var returnFetchJokeResult: JokeDataModel? = null
        var isSuccessful = true
        override suspend fun fetch(): JokeDataModel {
            return if (isSuccessful) {
                returnFetchJokeResult!!

            } else {
                throw java.lang.Exception()
            }
        }

        override fun chooseFavorite(fromCache: Boolean) {
            TODO("Not yet implemented")
        }

        var returnChangeJokeStatus: JokeDataModel? = null
        override suspend fun changeJokeStatus(): JokeDataModel {
            return returnChangeJokeStatus!!
        }
    }

    private class FakeCommunication : StateCommunication {
        var data: MutableList<State> = mutableListOf()

        override fun map(data: State) {
            this.data.add(data)
        }
    }
}

