package com.hayawanat.app.ui.match

import androidx.lifecycle.*
import com.hayawanat.app.data.AnimalData
import com.hayawanat.app.model.Animal

class MatchViewModel(private val level: Int) : ViewModel() {

    private val animals = AnimalData.getRandom(6, level)
    val totalPairs = animals.size
    var score = 0
        private set

    private val shuffledFrench = animals.shuffled()

    // arabic list (ordered), french list (shuffled)
    val pairs: LiveData<List<Pair<String, String>>> = MutableLiveData(
        animals.map { it.arabic to it.french }
    )

    val emojiFrenchPairs: List<Pair<String, String>> =
        shuffledFrench.map { it.emoji to it.french }

    // Triple: arabicIndex, frenchIndex, correct
    private val _matchResult = MutableLiveData<Triple<Int, Int, Boolean>?>()
    val matchResult: LiveData<Triple<Int, Int, Boolean>?> = _matchResult

    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    private val matched = mutableSetOf<Int>() // arabic indices matched

    fun tryMatch(arabicIndex: Int, frenchIndex: Int) {
        val arabicAnimal = animals[arabicIndex]
        val frenchAnimal = shuffledFrench[frenchIndex]
        val correct = arabicAnimal.id == frenchAnimal.id

        if (correct) {
            score++
            matched.add(arabicIndex)
            if (matched.size == totalPairs) {
                _matchResult.value = Triple(arabicIndex, frenchIndex, true)
                _gameFinished.value = true
                return
            }
        }
        _matchResult.value = Triple(arabicIndex, frenchIndex, correct)
    }
}

class MatchViewModelFactory(private val level: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MatchViewModel(level) as T
    }
}
