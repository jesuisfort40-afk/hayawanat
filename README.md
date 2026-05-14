# Hayawanat حيوانات 🐾

Application Android pour apprendre les noms des animaux en **arabe** et en **français**.

## 🎮 Modes de jeu

| Mode | Description |
|------|-------------|
| 🔗 **Relier** | Relie le mot arabe à sa traduction française |
| 🃏 **Mémory** | Retourne les paires arabe / français |
| 🧩 **Puzzle** | Complète les lettres manquantes du mot arabe |
| 🔊 **Écouter** | Écoute la prononciation arabe (TTS) et devine l'animal |
| ⚡ **Quiz** | QCM 4 choix avec timer 10 secondes |

## 📊 Niveaux

- **Débutant** : 10 animaux courants (lion, chat, chien…)
- **Intermédiaire** : 10 animaux plus difficiles (tigre, crocodile…)
- **Expert** : 10 animaux rares (caméléon, morse, paon…)

## 🏗 Stack technique

- Kotlin + Android Jetpack (Navigation, Room, ViewModel, LiveData)
- Material Design 3
- TextToSpeech Android (arabe natif, aucune API externe)
- GitHub Actions CI/CD → APK automatique à chaque push

## 🚀 Build

```bash
./gradlew assembleDebug
```

Le CI GitHub génère un APK signé automatiquement à chaque push sur `main`.

## 📁 Structure

```
app/src/main/java/com/hayawanat/app/
├── data/          AnimalData.kt (30 animaux)
├── db/            Room database (scores)
├── model/         Models, GameMode, Difficulty
└── ui/
    ├── home/      Écran d'accueil + choix du mode
    ├── match/     Jeu Relier
    ├── memory/    Jeu Mémory
    ├── puzzle/    Jeu Puzzle
    ├── listen/    Jeu Écouter
    ├── quiz/      Jeu Quiz QCM
    └── result/    Écran résultats + étoiles
```
