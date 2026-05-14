package com.hayawanat.app.ui.quiz

import androidx.lifecycle.*
import com.hayawanat.app.data.AnimalData
import com.hayawanat.app.model.Animal

data class QuizQuestion(
    val animal: Animal,
    val choices: List<String>,
    val correctIndex: Int
)

class QuizViewModel(private val level: Int) : ViewModel() {

    private val animals = AnimalData.getRandom(10, level)
    val totalQuestions = animals.size
    var questionIndex = 0
        private set
    var score = 0
        private set

    private val _currentQuestion = MutableLiveData<QuizQuestion?>()
    val currentQuestion: LiveData<QuizQuestion?> = _currentQuestion

    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    init { loadQuestion() }

    private fun loadQuestion() {
        if (questionIndex >= totalQuestions) {
            _gameFinished.value = true
            return
        }
        val animal = animals[questionIndex]
        val wrongAnswers = AnimalData.animals
            .filter { it.id != animal.id }
            .shuffled()
            .take(3)
            .map { it.french }
        val choices = (wrongAnswers + animal.french).shuffled()
        val correctIndex = choices.indexOf(animal.french)
        _currentQuestion.value = QuizQuestion(animal, choices, correctIndex)
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

class QuizViewModelFactory(private val level: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return QuizViewModel(level) as T
    }
}
