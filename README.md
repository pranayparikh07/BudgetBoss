# BudgetBoss (Java Edition) - World's Best Budget Management App 2025

This is a cutting-edge Native Android Application built with **Java** and **XML**, following Clean Architecture and MVVM patterns. Redesigned for 2025 with Material Design 3, advanced UI/UX, and premium aesthetics.

## 🚀 2025 Features & Design Highlights
- **Material Design 3**: Full Material3 theming with dynamic colors, typography, and components
- **Premium Dark Theme**: Navy, gold, and cyan color palette for a luxurious feel
- **Custom Icons**: Vector-based icons for all navigation and actions
- **Enhanced Cards**: Outlined MaterialCardViews with rounded corners and elevation
- **Modern Typography**: Comprehensive text appearances for hierarchy and readability
- **Improved Splash Screen**: Animated logo reveal with progress indicator
- **Accessibility**: Better contrast ratios and semantic elements
- **Responsive Layout**: Optimized for all screen sizes with ConstraintLayout

## Features implemented
- **Authentication**: Login & Register with Firebase Auth.
- **Dashboard**: Real-time balance, income, and expense summary with KPI cards.
- **Transactions**: Add Income/Expenses, view recent transactions list.
- **Synchronization**: Real-time sync between Local Room Database and Firebase Realtime Database.
- **Navigation**: Bottom Navigation with custom icons for Dashboard, Budget, Vault, Investments, Profile.
- **Quick Actions**: Horizontal scrollable action buttons for common tasks.
- **Analytics**: Chart visualization for monthly overview.
- **AI Integration**: Ask AI feature for financial insights.
- **Architecture**: Hilt (DI), Room (Local DB), Repository Pattern.

## Setup
1.  **Firebase**: Ensure `google-services.json` is present in the `app/` directory.
2.  **Build**: Run via Android Studio or `./gradlew build`.
3.  **Dependencies**: Java 8+, Android SDK 24+, Material Components 1.13.0+.

## Structure
- `presentation/`: Activities, Fragments, ViewModels with Material3 components.
- `domain/`: Models, Repository Interfaces.
- `data/`: Room Entities, DAOs, Repository Implementations.
- `di/`: Hilt Modules.
- `res/`: Material3 themes, custom vector icons, layouts.

## Design Philosophy
Crafted with 2025 design principles: minimalism, accessibility, performance, and user delight. The app combines premium aesthetics with functional excellence to provide the world's best budget management experience.
