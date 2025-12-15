<p align="center">
  <img src="https://github.com/user-attachments/assets/b95a4305-46f0-4e28-8196-361743885e3e" width="120" alt="BudgetBoss Logo"/>
</p>

<h1 align="center">💰 BudgetBoss</h1>

<p align="center">
  <strong>Your Personal Finance Companion</strong><br>
  A modern Android app for budget management, expense tracking, investments, and financial planning
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=flat&logo=openjdk" alt="Language"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue" alt="Target SDK"/>
  <img src="https://img.shields.io/badge/Material-3-6750A4?style=flat&logo=material-design" alt="Material 3"/>
  <img src="https://img.shields.io/badge/Firebase-Realtime%20DB-FFCA28?style=flat&logo=firebase" alt="Firebase"/>
</p>


---

## ✨ Features

### 🏠 Dashboard
- **Real-time Balance**: View total balance, income, and expenses at a glance
- **Quick Stats**: Visual KPI cards showing financial health
- **Recent Transactions**: Quick access to latest activities
- **Quick Actions**: One-tap access to common features

### 💳 Transaction Management
- **Add Income/Expenses**: Easy transaction entry with categories
- **Category Selection**: Food, Transport, Shopping, Bills, Entertainment, etc.
- **Payment Methods**: Cash, UPI, Card tracking
- **Receipt Scanner**: OCR-powered receipt scanning using ML Kit
- **Transaction History**: Searchable and filterable list

### 📊 Budget Goals
- **Category Budgets**: Set spending limits by category
- **Period Selection**: Weekly, Monthly, or Yearly budgets
- **Progress Tracking**: Visual indicators for budget utilization
- **Smart Alerts**: Notifications when approaching limits

### 📈 Investments & Loans
- **SIP Tracking**: Monitor Systematic Investment Plans
- **Stocks & Mutual Funds**: Track your portfolio
- **Loan Management**: Track loans given and taken
- **Interest Calculator**: Automatic return calculations

### 🔐 Vault (Secret Savings)
- **PIN Protected**: Secure access to hidden savings
- **Add/Withdraw Funds**: Manage secret savings
- **Payment Methods**: Track source of funds
- **Transaction History**: Complete vault activity log

### 👤 Profile & Settings
- **User Profile**: Personalized experience
- **Dark/Light Theme**: Automatic and manual theme switching
- **Notifications**: Customizable alerts
- **Data Export**: Export your financial data
- **Secure Logout**: Safe session management

### 🤖 AI Assistant
- **Financial Insights**: AI-powered spending analysis
- **Budget Recommendations**: Smart suggestions
- **Chatbot Interface**: Natural conversation UI

---

## 🎨 Design System

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| 🔵 Authority Blue | `#1E3A8A` | Primary brand color |
| 🔷 Blue Light | `#3B82F6` | Accents, links |
| 🟢 Profit Green | `#15803D` | Income, positive values |
| 🔴 Controlled Red | `#B91C1C` | Expenses, alerts |
| 🟡 Savings Gold | `#F59E0B` | Highlights, warnings |

### Typography
- **Headlines**: Bold, high contrast
- **Body**: Clear, readable
- **Labels**: Subtle, informative

### Components
- Material Design 3 components
- Custom rounded cards (16dp-28dp corners)
- Branded icons and illustrations
- Smooth animations and transitions

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────────────┐ │
│  │Activities│  │Fragments│  │ Adapters│  │    ViewModels   │ │
│  └─────────┘  └─────────┘  └─────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        Domain Layer                          │
│  ┌─────────────────┐              ┌─────────────────────┐   │
│  │     Models      │              │ Repository Interfaces│   │
│  └─────────────────┘              └─────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                         Data Layer                           │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────────────┐ │
│  │ Entities│  │   DAOs  │  │  Repos  │  │ Firebase Sync   │ │
│  └─────────┘  └─────────┘  └─────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
    ┌─────────────────┐             ┌─────────────────┐
    │   Room Database │             │ Firebase RTDB   │
    │     (Local)     │◄───Sync────►│    (Cloud)      │
    └─────────────────┘             └─────────────────┘
```

### Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Java 8 |
| **UI Framework** | Android XML + View Binding |
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Hilt 2.51.1 |
| **Local Database** | Room 2.6.1 |
| **Cloud Database** | Firebase Realtime Database |
| **Authentication** | Firebase Auth |
| **Navigation** | Jetpack Navigation 2.7.7 |
| **UI Components** | Material Design 3 (1.13.0) |
| **ML/AI** | Google ML Kit (Text Recognition) |
| **Lifecycle** | ViewModel + LiveData 2.8.0 |

---

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/budgetboss/
│   │   ├── BudgetBossApp.java          # Application class
│   │   ├── MainActivity.java            # Main activity with navigation
│   │   ├── SplashActivity.java          # Splash screen
│   │   │
│   │   ├── presentation/               # UI Layer
│   │   │   ├── intro/                  # First-launch intro screens
│   │   │   ├── auth/                   # Login, Register, Splash
│   │   │   ├── dashboard/              # Home dashboard
│   │   │   ├── transactions/           # Transaction management
│   │   │   ├── budget/                 # Budget goals
│   │   │   ├── investments/            # Investment tracking
│   │   │   ├── vault/                  # Secret savings
│   │   │   ├── profile/                # User profile
│   │   │   ├── settings/               # App settings
│   │   │   ├── analytics/              # Charts and reports
│   │   │   ├── ai/                     # AI chat assistant
│   │   │   ├── receipt/                # Receipt scanner
│   │   │   ├── notifications/          # Notification management
│   │   │   └── viewmodel/              # Shared ViewModels
│   │   │
│   │   ├── domain/                     # Business Logic
│   │   │   ├── models/                 # Domain models
│   │   │   └── repository/             # Repository interfaces
│   │   │
│   │   ├── data/                       # Data Layer
│   │   │   ├── local/                  # Room database
│   │   │   │   ├── AppDatabase.java
│   │   │   │   ├── dao/                # Data Access Objects
│   │   │   │   └── entity/             # Room entities
│   │   │   └── repository/             # Repository implementations
│   │   │
│   │   ├── di/                         # Hilt modules
│   │   │   ├── AppModule.java
│   │   │   ├── DatabaseModule.java
│   │   │   └── RepositoryModule.java
│   │   │
│   │   ├── utils/                      # Utilities
│   │   └── widget/                     # Home screen widget
│   │
│   └── res/
│       ├── layout/                     # XML layouts
│       ├── drawable/                   # Icons and shapes
│       ├── values/                     # Colors, strings, themes
│       ├── values-night/               # Dark theme colors
│       ├── menu/                       # Navigation menus
│       ├── navigation/                 # Nav graphs
│       └── xml/                        # Widget config
│
├── build.gradle                        # App-level build config
└── google-services.json                # Firebase config
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or newer
- **JDK**: 8 or higher
- **Android SDK**: API 24+ (Android 7.0)
- **Firebase Account**: For authentication and database

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/BudgetBoss.git
   cd BudgetBoss
   ```

2. **Firebase Setup**
   - Create a project at [Firebase Console](https://console.firebase.google.com)
   - Enable **Authentication** (Email/Password)
   - Enable **Realtime Database**
   - Download `google-services.json` and place in `app/` folder

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to cloned directory

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or press ▶️ Run in Android Studio

### Firebase Database Rules

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "transactions": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "budgets": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "investments": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "vault": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    }
  }
}
```

---

## 📋 API Reference

### Room Entities

| Entity | Description |
|--------|-------------|
| `UserEntity` | User profile data |
| `TransactionEntity` | Income/expense records |
| `BudgetGoalEntity` | Budget goals by category |
| `VaultTransactionEntity` | Secret savings transactions |

### Repository Methods

```java
// TransactionRepository
LiveData<List<Transaction>> getAllTransactions();
void addTransaction(Transaction transaction);
void deleteTransaction(Transaction transaction);

// BudgetRepository
LiveData<List<BudgetGoalEntity>> getAllBudgetGoals();
void addBudgetGoal(BudgetGoalEntity budget);
void updateBudgetGoal(BudgetGoalEntity budget);
void deleteBudgetGoal(BudgetGoalEntity budget);

// InvestmentRepository
LiveData<List<Investment>> getInvestments();
void addInvestment(Investment investment);
void updateInvestment(Investment investment);
void deleteInvestment(Investment investment);

// VaultRepository
LiveData<Double> getVaultBalance();
void addVaultTransaction(VaultTransaction transaction);
```

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

---

## 📦 Build Variants

| Variant | Description |
|---------|-------------|
| `debug` | Development build with logging |
| `release` | Production build with ProGuard |

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add comments for complex logic
- Write unit tests for new features

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 BudgetBoss Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 👥 Team

| Role | Contributor |
|------|-------------|
| 🎨 UI/UX Design | Shreya Oza & Vishwa Astik |
| 💻 Android Development | Pranay Parikh & Raj Kalotara |
| 🔥 Firebase Integration | Pranay Parikh & Raj Kalotara |
| 🧪 Testing & QA | Vishwa Astik |

---

## 🙏 Acknowledgments

- [Material Design 3](https://m3.material.io/) - Design system
- [Firebase](https://firebase.google.com/) - Backend services
- [Android Jetpack](https://developer.android.com/jetpack) - Architecture components
- [Google ML Kit](https://developers.google.com/ml-kit) - OCR capabilities

---

<p align="center">
  Made with ❤️ by the BudgetBoss Team
</p>

<p align="center">
  <a href="#-budgetboss">⬆️ Back to Top</a>
</p>
