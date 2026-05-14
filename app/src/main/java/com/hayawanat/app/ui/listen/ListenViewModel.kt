package com.hayawanat.app.ui.listen

import androidx.lifecycle.*
import com.hayawanat.app.data.AnimalData
import com.hayawanat.app.model.Animal

data class ListenQuestion(
    val animal: Animal,
    val choices: List<Animal>,
    val correctIndex: Int
)

class ListenViewModel(private val level: Int) : ViewModel() {

    private val animals = AnimalData.getRandom(10, level)
    val total = animals.size
    var questionIndex = 0
        private set
    var score = 0
        private set

    private val _currentQuestion = MutableLiveData<ListenQuestion?>()
    val currentQuestion: LiveData<ListenQuestion?> = _currentQuestion

    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    init { loadQuestion() }

    private fun loadQuestion() {
        if (questionIndex >= total) { _gameFinished.value = true; return }
        val animal = animals[questionIndex]
        val wrong = AnimalData.animals
            .filter { it.id != animal.id }
            .shuffled()
            .take(3)
        val choices = (wrong + animal).shuffled()
        _currentQuestion.value = ListenQuestion(animal, choices, choices.indexOf(animal))
    }

    fun checkAnswer(index: Int): Boolean {
        val correct = index == _currentQuestion.value?.correctIndex
        if (correct) score++
        return correct
    }

    fun nextQuestion() {
        questionIndex++
        loadQuestion()
    }
}

class ListenViewModelFactory(private val level: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ListenViewModel(level) as T
    }
}
