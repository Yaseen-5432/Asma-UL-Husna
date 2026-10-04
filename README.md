# Asma-UL-Husna

A modern Android application for exploring the **99 Names of Allah (Asma-ul-Husna)** through Arabic names, English and Urdu meanings, explanations, audio, full recitation, favorites, search, and interactive offline quizzes.

The application is designed with a clean, modern interface using **Jetpack Compose**, with a strong focus on accessibility, offline functionality, smooth animations, and an engaging learning experience.

---

## ✨ Features

### 📖 Explore the 99 Names of Allah

* Browse all **99 Names of Allah**
* View each name in Arabic
* English meaning
* Urdu meaning
* Detailed explanation
* Name-specific audio
* Full recitation experience
* Previous/Next navigation between names

### 🔎 Search

Quickly find a specific Name of Allah using the built-in search functionality.

### ❤️ Favorites

* Save your favorite Names
* Easily access saved names from the Favorites section
* Favorites remain available without requiring an account

### 🎧 Audio

The application provides audio for individual Names.

* Play individual Name audio
* Play/pause controls
* Audio playback handled directly within the application
* No account required

### 🎙️ Full Recitation

Listen to the complete recitation of the Names of Allah.

The recitation experience includes:

* Full audio playback
* Name-by-name synchronization
* Automatic identification of the currently recited Name
* Smooth scrolling
* Visual highlighting
* Animated transitions between Names

### 🔊 Explanation Text-to-Speech

The Explanation section includes a native Android Text-to-Speech option.

* English explanation can be spoken aloud
* Urdu explanation can be spoken when an Urdu voice is available on the device
* Uses Android's built-in Text-to-Speech functionality
* No cloud AI or external speech API is required

### 🧠 Offline Quiz

The application includes an interactive quiz designed to help users learn and remember the Names of Allah.

* Completely offline
* No login required
* No API required
* Questions based on the application's existing Name meanings and explanations
* Four options per question
* Randomized quiz questions and answer options
* Score tracking
* Result screen
* Answer review
* Colorful animations and interactive feedback

The quiz content is stored locally in the application and does not depend on an internet connection.

### 🎨 Modern UI

The application uses a modern Compose-based interface with:

* Material-style components
* Modern cards
* Smooth transitions
* Interactive animations
* Colorful quiz experience
* Light and dark theme support
* Responsive layouts
* Animated Name transitions

### 🎁 Interactive Name Transitions

Opening a Name from the Home screen includes an interactive transition designed to make navigation more engaging.

The application also provides animated transitions when moving between Names using the Previous and Next controls.

---

## 📱 Screens

The application includes several main sections:

* **Home** — Browse and discover Names
* **Search** — Find Names quickly
* **Favorites** — Access saved Names
* **Quiz** — Test knowledge of the Names
* **Name Details** — Explore complete information about a Name

---

## 🛠️ Tech Stack

### Android

* **Kotlin**
* **Jetpack Compose**
* **Android SDK**
* **Material Design / Compose UI**
* **MVVM architecture**
* **Hilt** for dependency injection
* **Kotlin Coroutines**
* **Flow / StateFlow**
* **Local JSON data**
* **Android Text-to-Speech**
* **Media3 / ExoPlayer** for audio playback

The project is designed around modern Android development practices while keeping the application lightweight and largely offline.

---

## 🏗️ Architecture

The application follows a structured architecture based around **MVVM**, with separation between UI, state management, data, and application logic.

A simplified flow is:

```text
UI
 ↓
ViewModel
 ↓
Use Case / Application Logic
 ↓
Repository
 ↓
Local Data / Android Services
```

This structure helps keep the application maintainable and makes it easier to add future functionality without unnecessarily affecting existing features.

---

## 📂 Project Structure

The exact structure may evolve as the project develops, but the application follows a separation similar to:

```text
app/
└── src/
    └── main/
        ├── java/
        │   └── ...
        │
        ├── res/
        │   └── ...
        │
        └── assets/
            └── ...
```

Application components are separated according to their responsibilities, including:

```text
UI / Screens
ViewModels
Repositories
Models
Use Cases
Audio
Navigation
Quiz
Theme
Utilities
```

---

## 🧠 Quiz System

The Quiz feature is completely independent from the main Name data.

Quiz questions are stored in a separate local JSON file so that the existing Name data does not need to be modified.

The quiz content covers:

* Name meanings
* Name explanations
* Understanding-based questions

Each question contains:

```text
Question
├── English / Urdu text
├── 4 answer options
└── Correct answer
```

The application generates quiz sessions from this local data and handles scoring entirely on the device.

No:

* AI API
* Backend
* Firebase
* Login
* Online database

is required for the Quiz feature.

---

## 🎧 Audio & Recitation

Audio functionality is divided into two experiences.

### Individual Name Audio

Each Name can have its own audio playback.

### Full Recitation

The application also provides a complete recitation experience with synchronized Name transitions.

During full recitation, the application can:

1. Play the complete recitation
2. Determine the current Name from timestamps
3. Highlight the active Name
4. Scroll toward the active Name
5. Animate the active Name
6. Continue until the complete recitation finishes

---

## 🔐 Privacy & Offline Usage

The application does not require users to create an account.

Several core features work completely offline, including:

* Browsing Names
* Searching Names
* Favorites
* Quiz
* Text-to-Speech functionality, when the required device voice is installed
* Full recitation using bundled application resources

The application is designed to minimize unnecessary dependence on external services.

---

## 🌐 Cross-Platform Project

**Asma-UL-Husna** is part of a broader cross-platform project.

The Android application and the corresponding web application share the same overall vision of providing an accessible and modern way to explore the 99 Names of Allah.

### Web Application

The web application counterpart was developed by:

**Anum-projects**

GitHub: `@Anum-projects`

Special thanks to **Anum-projects** for developing the web application counterpart and contributing to the shared project vision, design direction, and cross-platform experience.

---

## 🚀 Getting Started

### Requirements

To build and run the Android application, you will need:

* Android Studio
* Android SDK
* A compatible JDK
* Android device or emulator

### Clone the Repository

```bash
git clone https://github.com/Yaseen-5432/Asma-UL-Husna.git
```

### Open the Project

1. Open **Android Studio**
2. Select **Open**
3. Select the cloned `Asma-UL-Husna` directory
4. Allow Gradle to synchronize
5. Connect an Android device or start an emulator
6. Run the application

> The repository's current Android Studio and Gradle configuration should be used when building the project. Avoid changing build versions unless required by the development environment.

---

## 🧪 Testing

The application should be tested on both emulator and physical Android devices where possible.

Important areas to verify include:

* Home screen
* Name details
* Search
* Favorites
* Individual audio
* Full recitation
* Previous/Next navigation
* Text-to-Speech
* Quiz questions
* Quiz scoring
* Quiz animations
* Light/Dark themes
* Offline functionality
* Screen navigation and lifecycle behavior

---

## 🎯 Project Goals

The main goals of **Asma-UL-Husna** are to provide:

* A simple way to explore the 99 Names of Allah
* Arabic, English, and Urdu information
* An engaging learning experience
* Accessible audio experiences
* Offline learning capabilities
* A modern Android user interface
* A maintainable and scalable Android architecture

The project also provides a foundation for future improvements while keeping the current application focused and lightweight.

---

## 🔮 Future Improvements

Possible future improvements may include:

* Additional accessibility improvements
* More learning and revision modes
* Additional audio options
* Improved personalization
* More detailed learning statistics
* Additional cross-platform synchronization

Future features will be considered carefully so that they do not unnecessarily complicate the core offline experience.

---

## 🤝 Contributions

Contributions, suggestions, and improvements are welcome.

If you would like to contribute:

1. Fork the repository
2. Create a new branch

```bash
git checkout -b feature/your-feature
```

3. Make your changes
4. Test the application
5. Commit your changes

```bash
git commit -m "Add your feature"
```

6. Push the branch

```bash
git push origin feature/your-feature
```

7. Open a Pull Request

Please keep changes focused and avoid modifying unrelated parts of the application.

---

## 📄 License

This project does not currently specify a license.

If you intend to allow others to use, modify, or redistribute the source code, add an appropriate open-source license to the repository.

For example, a `LICENSE` file can be added using a license such as MIT, Apache-2.0, or another license that matches the project's intended usage.

---

## 🙏 Credits

### Android Application

**Asma-UL-Husna Android Application**

Developed using Kotlin and Jetpack Compose.

### Web Application

**Anum-projects**

Developed the web application counterpart of the project and contributed to the shared cross-platform vision and design direction.

---

## 📌 Project Status

**Active Development**

The application is actively being developed and improved. Features, UI, architecture, and documentation may continue to evolve over time.

---

## 🌙 About Asma-ul-Husna

**Asma-ul-Husna** refers to the Beautiful Names of Allah.

This application is intended as a digital learning and exploration tool, bringing the Names, their meanings, explanations, audio, and learning activities together in one modern Android experience.

---

**Built with Kotlin + Jetpack Compose ❤️**
