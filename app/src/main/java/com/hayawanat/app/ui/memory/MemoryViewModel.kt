package com.hayawanat.app.ui.memory

import androidx.lifecycle.*
import com.hayawanat.app.data.AnimalData
import kotlinx.coroutines.*

data class MemoryCard(
    val id: Int,
    val pairId: Int,       // same for arabic+french of same animal
    val content: String,   // arabic word OR emoji+french
    val isArabic: Boolean,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

class MemoryViewModel(private val level: Int) : ViewModel() {

    private val animals = AnimalData.getRandom(6, level)
    val totalPairs = animals.size
    var matchedPairs = 0
        private set

    private val _cards = MutableLiveData<List<MemoryCard>>()
    val cards: LiveData<List<MemoryCard>> = _cards

    private val _attempts = MutableLiveData(0)
    val attempts: LiveData<Int> = _attempts

    private val _gameFinished = MutableLiveData(false)
    val gameFinished: LiveData<Boolean> = _gameFinished

    private var firstFlipped: MemoryCard? = null
    private var isChecking = false

    init {
        val cardList = mutableListOf<MemoryCard>()
        animals.forEachIndexed { i, animal ->
            cardList.add(MemoryCard(i * 2,     animal.id, animal.arabic,             true))
            cardList.add(MemoryCard(i * 2 + 1, animal.id, "${animal.emoji} ${animal.french}", false))
        }
        _cards.value = cardList.shuffled()
    }

    fun flipCard(position: Int) {
        if (isChecking) return
        val current = _cards.value?.toMutableList() ?: return
        val card = current[position]
        if (card.isFlipped || card.isMatched) return

        card.isFlipped = true
        _cards.value = current

        if (firstFlipped == null) {
            firstFlipped = card
        } else {
            isChecking = true
            _attempts.value = (_attempts.value ?: 0) + 1
            val first = firstFlipped!!
            firstFlipped = null

            if (first.pairId == card.pairId) {
                // Match found
                val updated = current.map {
                    if (it.pairId == card.pairId) it.copy(isMatched = true) else it
                }
                _cards.value = updated
                matchedPairs++
                isChecking = false
                if (matchedPairs == totalPairs) _gameFinished.value = true
            } else {
                // No match — flip back after delay
                viewModelScope.launch {
                    delay(900)
                    val reset = _cards.value?.toMutableList() ?: return@launch
                    reset.forEachIndexed { i, c ->
                        if (!c.isMatched) reset[i] = c.copy(isFlipped = false)
                    }
                    _cards.value = reset
                    isChecking = false
                }
            }
        }
    }
}

class MemoryViewModelFactory(private val level: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MemoryViewModel(level) as T
    }
}
