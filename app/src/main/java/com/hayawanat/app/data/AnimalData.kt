package com.hayawanat.app.data

import com.hayawanat.app.model.Animal

object AnimalData {

    val animals = listOf(
        // ── Niveau 1 : Débutant ──────────────────────────────────────────
        Animal(1,  "أسد",     "asad",       "lion",          "🦁", 1),
        Animal(2,  "قطة",     "qitta",      "chat",          "🐱", 1),
        Animal(3,  "كلب",     "kalb",       "chien",         "🐶", 1),
        Animal(4,  "حصان",    "hisan",      "cheval",        "🐴", 1),
        Animal(5,  "بقرة",    "baqara",     "vache",         "🐄", 1),
        Animal(6,  "دجاجة",   "dajaja",     "poule",         "🐔", 1),
        Animal(7,  "سمكة",    "samaka",     "poisson",       "🐟", 1),
        Animal(8,  "طائر",    "ta'ir",      "oiseau",        "🐦", 1),
        Animal(9,  "فيل",     "fil",        "éléphant",      "🐘", 1),
        Animal(10, "أرنب",    "arnab",      "lapin",         "🐰", 1),

        // ── Niveau 2 : Intermédiaire ─────────────────────────────────────
        Animal(11, "نمر",     "namir",      "tigre",         "🐯", 2),
        Animal(12, "دب",      "dubb",       "ours",          "🐻", 2),
        Animal(13, "ذئب",     "dhi'b",      "loup",          "🐺", 2),
        Animal(14, "قرد",     "qird",       "singe",         "🐒", 2),
        Animal(15, "زرافة",   "zarafa",     "girafe",        "🦒", 2),
        Animal(16, "تمساح",   "timsah",     "crocodile",     "🐊", 2),
        Animal(17, "ضفدع",    "dufda'",     "grenouille",    "🐸", 2),
        Animal(18, "بطة",     "batta",      "canard",        "🦆", 2),
        Animal(19, "خروف",    "kharuf",     "mouton",        "🐑", 2),
        Animal(20, "خنزير",   "khinzir",    "cochon",        "🐷", 2),

        // ── Niveau 3 : Expert ────────────────────────────────────────────
        Animal(21, "فراشة",   "farasha",    "papillon",      "🦋", 3),
        Animal(22, "عقرب",    "aqrab",      "scorpion",      "🦂", 3),
        Animal(23, "أخطبوط",  "ukhtabut",   "pieuvre",       "🐙", 3),
        Animal(24, "دلفين",   "dulfin",     "dauphin",       "🐬", 3),
        Animal(25, "طاووس",   "ta'us",      "paon",          "🦚", 3),
        Animal(26, "نسر",     "nasr",       "aigle",         "🦅", 3),
        Animal(27, "حرباء",   "hirba'",     "caméléon",      "🦎", 3),
        Animal(28, "فيل البحر","fil al-bahr","morse",        "🦭", 3),
        Animal(29, "جمل",     "jamal",      "chameau",       "🐪", 3),
        Animal(30, "خفاش",    "khuffash",   "chauve-souris", "🦇", 3)
    )

    fun getByLevel(level: Int): List<Animal> =
        if (level == 0) animals else animals.filter { it.level == level }

    fun getRandom(count: Int, level: Int = 0): List<Animal> =
        getByLevel(level).shuffled().take(count)
}
