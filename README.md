# BudgetBoss (Java Edition)

This is a Native Android Application built with **Java** and **XML**, following Clean Architecture and MVVM patterns.

## Features implemented
- **Authentication**: Login & Register with Firebase Auth.
- **Dashboard**: Real-time balance, income, and expense summary.
- **Transactions**: Add Income/Expenses, view recent transactions list.
- **Synchronization**: Real-time sync between Local Room Database and Firebase Realtime Database.
- **Navigation**: Bottom Navigation for Dashboard, Budget, and Vault.
- **Architecture**: Hilt (DI), Room (Local DB), Repository Pattern.

## Setup
1.  **Firebase**: Ensure `google-services.json` is present in the `app/` directory.
2.  **Build**: Run via Android Studio.
3.  **Dependencies**: Java 8+, Android SDK 24+.

## Structure
- `presentation/`: Activities, Fragments, ViewModels.
- `domain/`: Models, Repository Interfaces.
- `data/`: Room Entities, DAOs, Repository Implementations.
- `di/`: Hilt Modules.
