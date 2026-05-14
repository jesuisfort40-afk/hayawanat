package com.hayawanat.app.model

data class Animal(
    val id: Int,
    val arabic: String,          // الأسد
    val transliteration: String, // al-asad
    val french: String,          // lion
    val emoji: String,           // 🦁
    val level: Int               // 1=débutant, 2=intermédiaire, 3=expert
)

enum class GameMode {
    MATCH,   // Relier
    MEMORY,  // Mémory
    PUZZLE,  // Compléter le mot
    LISTEN,  // Écouter et deviner
    QUIZ     // QCM
}

enum class Difficulty(val label: String, val level: Int) {
    BEGINNER("Débutant", 1),
    INTERMEDIATE("Intermédiaire", 2),
    EXPERT("Expert", 3),
    ALL("Tous", 0)
}
