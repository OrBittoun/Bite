# Bite 🍔

**Bite** is a native Android application developed as part of an academic project. The app offers a comprehensive experience for exploring international cuisines, managing personal favorites, and interacting with a community of food enthusiasts through ratings and comments.

The project demonstrates a deep understanding of **Clean Architecture**, the **MVVM** pattern, and the integration of multiple data sources (Local SQLite via Room & Remote REST API via Retrofit).

---

## 🌟 Features

* **Dynamic Home Screen**
* **Kitchen Categories**: Horizontal scrolling previews for localized kitchens like Italian, Asian, and Vegan.
* **Explore Section**: Real-time integration with *TheMealDB* API, allowing users to discover global recipes.
* **My Favorites ❤️**: A unified favorites row on the home screen that seamlessly displays dishes saved from both the local database and the remote API.


* **Hybrid Support**: Intelligent loading logic that handles both local resource IDs and remote URLs within the same UI components.


* **Modern Adaptive UI**
* **Full Dark Mode Support**: Entirely theme-aware UI using semantic color resources , ensuring accessibility and comfort in low-light environments.



* **Interactive Community Features**
* **Social Feedback**: Add, edit, or delete personal reviews and star-based ratings (1-5).
* **Data Synchronization**: Automatic local updates of review counts and favorite statuses using Room observers.
* **Lottie Animations**: Engaging success animations for user actions like posting comments.


* **Authentication & Security**
* **Firebase Integration**: Secure user registration and login powered by Firebase Authentication.
* **Cloud Profiles**: User profile data stored and synchronized via Cloud Firebase.



---

## 🛠 Tech Stack

* **Language**: Kotlin
* **Networking**: Retrofit & OkHttp (TheMealDB API)
* **Image Processing**: Glide
* **Database**: Room (Local Persistence)
* **Architecture**: MVVM with Hilt (Dependency Injection)
* **Concurrency**: Kotlin Coroutines & LiveData
* **UI/UX**: Material Design 3, XML, Lottie Animations, Navigation Component
