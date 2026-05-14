package com.hayawanat.app.ui.puzzle

import androidx.lifecycle.*
import com.hayawanat.app.data.AnimalData
import com.hayawanat.app.model.Animal

data class PuzzleQuestion(
    val animal: Animal,
    var maskedWord: String,
    val missingLetters: MutableList<String>,
    val choices: List<String>,
    var remaining: MutableList<String>
)

class PuzzleViewModel(private val level: Int) : ViewModel() {

    private val animals = AnimalData.getRandom(8, level)
    val total = animals.size
    var questionIndex = 0
        private set
    var score = 0
        private set

    private val _currentPuzzle = MutableLiveData<PuzzleQuestion?>()
    val currentPuzzle: LiveData<PuzzleQuestion?> = _currentPuzzle

    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    init { loadPuzzle() }

    private fun loadPuzzle() {
        if (questionIndex >= total) { _gameFinished.value = true; return }
        val animal = animals[questionIndex]
        val word = animal.arabic
        if (word.length < 2) { questionIndex++; loadPuzzle(); return }

        // Hide ~half the letters
        val indices = word.indices.toMutableList().shuffled()
            .take((word.length / 2).coerceAtLeast(1))
        val missing = indices.map { word[it].toString() }.toMutableList()

        var masked = word
        indices.forEach { i -> masked = masked.substring(0, i) + "_" + masked.substring(i + 1) }

        // Wrong letter choices from other animals
        val wrongLetters = AnimalData.animals
            .filter { it.id != animal.id }
            .flatMap { it.arabic.map { c -> c.toString() } }
            .distinct()
            .filter { it !in missing }
            .shuffled()
            .take((4 - missing.size).coerceAtLeast(0))

        val choices = (missing + wrongLetters).shuffled()

        _currentPuzzle.value = PuzzleQuestion(
            animal, masked, missing.toMutableList(), choices, missing.toMutableList()
        )
    }

    fun tryLetter(letter: String): Boolean {
        val puzzle = _currentPuzzle.value ?: return false
        val remaining = puzzle.remaining
        if (letter !in remaining) return false

        remaining.remove(letter)
        // Replace first _ with the letter in masked word
        val idx = puzzle.maskedWord.indexOf('_')
        if (idx >= 0) {
            puzzle.maskedWord = puzzle.maskedWord.substring(0, idx) +
                    letter + puzzle.maskedWord.substring(idx + 1)
        }
        _currentPuzzle.value = puzzle
        return true
    }

    fun isWordComplete(): Boolean {
        val puzzle = _currentPuzzle.value ?: return false
        val complete = puzzle.remaining.isEmpty()
        if (complete) score++
        return complete
    }

    fun nextQuestion() {
        questionIndex++
        loadPuzzle()
    }
}

class PuzzleViewModelFactory(private val level: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PuzzleViewModel(level) as T
    }
}
