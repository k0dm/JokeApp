package com.example.jokeapp.presentation.joke

import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.presentation.CommonViewModel
import com.example.jokeapp.presentation.StateCommunication


class JokesViewModel(interactor: CommonInteractor<Int>, communication: StateCommunication<Int>) :
    CommonViewModel.Abstract<Int>("JokesVM", interactor, communication)
