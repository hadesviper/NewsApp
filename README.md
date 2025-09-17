# Android News App 📰

This is a **simple news application** built using **Kotlin**, **Jetpack Compose**, and a modern **MVI clean architecture** approach. The app allows users to browse, search, and save news articles for later reading.

-----

## ⚙️ Features

  * **Onboarding:** A guided setup for new users to select their preferred country and news categories.
  * **Latest News:** Displays the latest news articles from the user's selected country.
  * **Search:** Allows users to search for articles and filter by different categories.
  * **Saved Articles:** Provides a dedicated section to view and manage articles saved for later.
  * **Intuitive UI:** A modern and responsive user interface built with Jetpack Compose.
  * **Offline Access:** Articles are cached locally using Room, allowing for offline viewing of previously loaded content.

-----

## 🛠️ Technology Stack

  * **Kotlin:** The primary programming language used for development.
  * **Jetpack Compose:** Used for building the declarative UI.
  * **MVI (Model-View-Intent):** The architectural pattern for managing state and business logic.
  * **Clean Architecture:** The project is structured with a clear separation of concerns (presentation, domain, and data layers).
  * **Dagger Hilt:** A dependency injection framework for managing dependencies throughout the app.
  * **Retrofit:** A type-safe HTTP client for making network calls to fetch news data.
  * **Room:** A persistence library for caching data locally.
  * **Coroutines & Flow:** Used for asynchronous operations and handling data streams.

-----

## 🖼️ Screenshots

-----

## 📂 Project Structure

The project is divided into three main layers following the principles of Clean Architecture:

  * **`presentation`:** Contains the UI components and business logic related to presenting data to the user. This includes composable functions, ViewModels, and UI state.
  * **`domain`:** The core business logic layer. This is an independent layer that contains use cases and repository interfaces.
  * **`data`:** The data layer responsible for providing data to the domain layer. It includes network calls (Retrofit), local caching (Room), and data mappers to transform data models.

-----

## 📲 Download

You can download the APK directly [here](https://github.com/hadesviper/NewsApp/releases/download/first_release/app-debug.apk)

---

## 👨‍💻 Author

**Ibrahim Abdin**

- GitHub: [GitHub](https://github.com/hadesviper)
- LinkedIn: [LinkedIn](https://linkedin.com/in/ibrahim-abdin-7ab463169)
