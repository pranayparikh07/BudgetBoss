# BudgetBoss - Personal Finance Manager

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" width="120" alt="BudgetBoss Logo"/>
</p>

<p align="center">
  <strong>Take Control of Your Financial Future</strong>
</p>

---

## 📱 Overview

**BudgetBoss** is a comprehensive personal finance management Android application designed to help users track expenses, manage budgets, secure savings in a virtual vault, and monitor investments & loans. Built with modern Android architecture and Material Design 3, it provides a professional, intuitive experience for managing personal finances.

---

## ✨ Features

### 🏠 Dashboard
- **Financial Overview**: Real-time display of total balance, income, and expenses
- **Quick Stats**: Visual cards showing key financial metrics
- **Recent Transactions**: Quick access to latest financial activities
- **Quick Actions**: Fast access to add income/expense

### 💰 Transaction Management
- **Add Income/Expense**: Easy transaction entry with categories
- **Receipt Scanner**: Camera-powered OCR to auto-extract transaction details
- **Category Management**: Organize transactions by custom categories
- **Transaction History**: Searchable, filterable transaction list
- **Empty States**: Helpful prompts when no data exists

### 📊 Budget Goals
- **Create Budgets**: Set spending limits by category
- **Period Selection**: Weekly, Monthly, or Yearly budget periods
- **Progress Tracking**: Visual progress bars showing budget usage
- **Edit/Delete**: Full CRUD operations on budgets
- **Overspending Alerts**: Visual indicators when approaching limits

### 🔐 Secure Vault
- **Virtual Savings**: Separate secure storage for savings
- **Add/Withdraw Funds**: Deposit or withdraw with payment method selection
- **Payment Methods**: Cash or UPI options
- **Balance Tracking**: Real-time vault balance display
- **Quick Actions**: One-tap add/withdraw buttons

### 📈 Investments & Loans
- **Investment Types**:
  - SIP (Systematic Investment Plan)
  - Stocks
  - Mutual Funds
  - Fixed Deposits
- **Loan Tracking**:
  - Loans Given
  - Loans Taken
- **Return Calculator**: Real-time calculation of expected returns
- **Interest Calculation**: Automatic interest computation for loans

### 👤 Profile & Settings
- **User Profile**: Display user information and avatar
- **Theme Toggle**: Switch between Light and Dark modes
- **App Settings**: Customize app behavior
- **About Section**: App information and credits
- **Sign Out**: Secure logout functionality

### 🎬 First Launch Experience
- **Intro Screen**: Chalkboard-style animated intro (shows only once)
- **Three Words**: "TRACK", "SAVE", "GROW" appear one after another
- **Chalk Font**: Hand-drawn chalk writing animation effect
- **Dark Chalkboard**: Green-tinted blackboard with wooden frame
- **Smooth Transition**: Fade out to main app after animation

---

## 🎨 Design System

### Color Palette

The app uses a professional "Authority" color scheme designed for financial applications:

| Color | Hex Code | Usage |
|-------|----------|-------|
| **Authority Blue** | `#1E3A8A` | Primary brand color, buttons, headers |
| **Blue Light** | `#3B82F6` | Accents, links, interactive elements |
| **Blue Dark** | `#172554` | Dark mode primary |
| **Profit Green** | `#15803D` | Income, positive values, success |
| **Green Light** | `#22C55E` | Light mode income indicators |
| **Controlled Red** | `#B91C1C` | Expenses, negative values, errors |
| **Red Light** | `#EF4444` | Dark mode expense indicators |

### Status Colors
- **Income**: `#16A34A` (Green)
- **Expense**: `#DC2626` (Red)
- **Savings**: `#2563EB` (Blue)
- **Neutral**: `#64748B` (Gray)

### Typography
- **Primary Text**: `#111827` (Light) / `#F1F5F9` (Dark)
- **Secondary Text**: `#4B5563` (Light) / `#94A3B8` (Dark)

### Theme Support
- ☀️ **Light Mode**: Clean white backgrounds with subtle shadows
- 🌙 **Dark Mode**: Deep navy backgrounds for reduced eye strain

---

## 🏗️ Architecture

### Tech Stack

| Component | Technology |
|-----------|------------|
| **Language** | Java |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 (Android 14) |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Dependency Injection** | Hilt |
| **Database** | Room (SQLite) |
| **Authentication** | Firebase Auth |
| **Navigation** | Jetpack Navigation Component |
| **UI Framework** | Material Design 3 |
| **Data Binding** | View Binding |

### Project Structure

```
app/
├── src/main/
│   ├── java/com/example/budgetboss/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── dao/           # Room DAOs
│   │   │   │   ├── database/      # Room Database
│   │   │   │   └── entity/        # Database Entities
│   │   │   └── repository/        # Data Repositories
│   │   ├── di/                    # Hilt Modules
│   │   ├── domain/
│   │   │   └── models/            # Domain Models
│   │   └── presentation/
│   │       ├── auth/              # Login/Register
│   │       ├── budget/            # Budget Management
│   │       ├── dashboard/         # Home Dashboard
│   │       ├── investments/       # Investments & Loans
│   │       ├── profile/           # User Profile
│   │       ├── settings/          # App Settings
│   │       ├── transactions/      # Transaction Management
│   │       └── vault/             # Secure Vault
│   └── res/
│       ├── drawable/              # Icons & Drawables
│       ├── layout/                # XML Layouts
│       ├── menu/                  # Navigation Menus
│       ├── navigation/            # Nav Graph
│       ├── values/                # Colors, Strings, Themes
│       └── values-night/          # Dark Mode Resources
```

### Key Components

#### ViewModels
- `DashboardViewModel` - Dashboard data management
- `TransactionViewModel` - Transaction CRUD operations
- `BudgetViewModel` - Budget goal management
- `VaultViewModel` - Vault balance operations
- `InvestmentViewModel` - Investment tracking
- `ProfileViewModel` - User profile data

#### Entities
- `TransactionEntity` - Income/Expense records
- `BudgetGoalEntity` - Budget configurations
- `VaultEntity` - Vault balance storage
- `Investment` - Investment/Loan records

---

## 📱 Screens

### 1. Dashboard (`fragment_dashboard.xml`)
- Welcome header with user greeting
- Balance overview card
- Income/Expense summary cards
- Recent transactions list
- Quick action FABs

### 2. Add Transaction (`fragment_add_transaction.xml`)
- Income/Expense toggle
- Amount input with currency
- Category selection
- Date picker
- Note/Description field
- Receipt scanner button

### 3. Transactions List (`fragment_transactions.xml`)
- Search functionality
- Filter by type/category
- Grouped by date
- Swipe actions for edit/delete

### 4. Budget (`fragment_budget.xml`)
- Budget cards with progress bars
- Spent vs Limit display
- Period indicator (Weekly/Monthly/Yearly)
- Add budget FAB
- Empty state for new users

### 5. Vault (`fragment_vault.xml`)
- Large balance display
- Add funds button
- Withdraw funds button
- Transaction history
- Security indicator

### 6. Investments (`fragment_investments.xml`)
- Investment/Loan list
- Type indicators (SIP, Stock, Loan, etc.)
- Amount and rate display
- Add investment FAB
- Return calculator

### 7. Profile (`fragment_profile.xml`)
- User avatar
- Name and email display
- Settings shortcuts
- Theme toggle
- About app
- Sign out button

---

## 🎯 Custom Dialogs

All dialogs feature:
- Material Card wrapper with 28dp corner radius
- Dim background (70% opacity)
- Smooth animations
- Proper keyboard handling

### Available Dialogs
1. **Add Budget Dialog** - Create/Edit budget goals
2. **Vault Transaction Dialog** - Add/Withdraw with payment method
3. **Add Investment Dialog** - Create investments/loans with type chips
4. **Investment Options Dialog** - Edit/Delete actions
5. **Delete Confirmation Dialog** - Warning before deletion
6. **About Dialog** - App information

---

## 🔧 Build Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Build Steps

```bash
# Clone the repository
git clone <repository-url>
cd BudgetBoss

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

### Configuration

1. **Firebase Setup**:
   - Add your `google-services.json` to `app/` directory
   - Enable Authentication in Firebase Console

2. **Signing Config** (for release):
   - Create `keystore.properties` file
   - Configure signing in `app/build.gradle`

---

## 📦 Dependencies

```groovy
// Core Android
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'

// Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
implementation 'androidx.navigation:navigation-fragment:2.7.6'
implementation 'androidx.navigation:navigation-ui:2.7.6'

// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'

// Hilt Dependency Injection
implementation 'com.google.dagger:hilt-android:2.48'
annotationProcessor 'com.google.dagger:hilt-compiler:2.48'

// Firebase
implementation platform('com.google.firebase:firebase-bom:32.7.0')
implementation 'com.google.firebase:firebase-auth'

// ML Kit (OCR)
implementation 'com.google.mlkit:text-recognition:16.0.0'

// Charts
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
```

---

## 🎨 Icons Reference

| Icon | File | Usage |
|------|------|-------|
| 🏠 | `ic_home.xml` | Dashboard/Home |
| 💰 | `ic_budget.xml` | Budget section |
| 🔐 | `ic_vault.xml` | Vault section |
| 📈 | `ic_investments.xml` | Investments |
| 👤 | `ic_profile.xml` | Profile section |
| ➕ | `ic_add.xml` | Add actions |
| 💵 | `ic_add_income.xml` | Income entry |
| 💸 | `ic_add_expense.xml` | Expense entry |
| ✏️ | `ic_edit.xml` | Edit actions |
| 🗑️ | `ic_delete.xml` | Delete actions |
| 📷 | `ic_camera.xml` | Receipt scanner |
| ⚠️ | `ic_warning.xml` | Warnings/Alerts |
| ✓ | `ic_check.xml` | Confirmations |
| 🏦 | `ic_account_balance.xml` | Loans |
| 📊 | `ic_trending_up.xml` | Returns/Growth |

---

## 🔒 Security Features

- **Firebase Authentication**: Secure user login/registration
- **Local Data Storage**: Room database with encryption support
- **Secure Vault**: Isolated savings storage
- **Input Validation**: All user inputs validated
- **Error Handling**: Graceful error messages

---

## 🚀 Future Enhancements

- [ ] Cloud sync with Firebase Firestore
- [ ] Export reports to PDF/Excel
- [ ] Bill reminders and notifications
- [ ] Multiple currency support
- [ ] Biometric authentication
- [ ] Widget support
- [ ] Recurring transactions
- [ ] Financial insights with AI

---

## 📄 License

```
Copyright 2025 BudgetBoss

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## � Team & Work Distribution

### Team Members

| Member | Role | Primary Responsibility |
|--------|------|------------------------|
| **Member 1** | Frontend Lead | UI/UX Design, Layouts, Themes |
| **Member 2** | Backend Lead | Database, Repositories, Business Logic |
| **Member 3** | Features Developer | Transactions, Budget, Vault Modules |
| **Member 4** | Integration Lead | Firebase, Camera/OCR, Testing |

---

## 📋 Detailed Work Distribution

### 👤 Member 1 - Frontend Lead (UI/UX)

**Responsibilities:** User Interface Design, XML Layouts, Styling, Theme System

#### Files Owned:
```
res/
├── values/
│   ├── colors.xml              # Color palette definition
│   ├── themes.xml              # Light theme styling
│   └── strings.xml             # App strings
├── values-night/
│   └── themes.xml              # Dark theme styling
├── drawable/
│   ├── bg_card.xml             # Card backgrounds
│   ├── bg_button_primary.xml   # Button styles
│   ├── bg_icon_circle.xml      # Icon containers
│   ├── ic_*.xml                # All vector icons
│   └── ripple_*.xml            # Touch feedback
├── layout/
│   ├── activity_main.xml       # Main container
│   ├── fragment_dashboard.xml  # Dashboard UI
│   ├── fragment_profile.xml    # Profile screen
│   ├── item_transaction.xml    # Transaction list item
│   ├── item_budget.xml         # Budget list item
│   ├── item_investment.xml     # Investment list item
│   └── layout_empty_state.xml  # Empty state component
└── menu/
    └── bottom_nav_menu.xml     # Navigation menu
```

#### Key Contributions:
- ✅ Designed "Authority Blue" color scheme (#1E3A8A, #15803D, #B91C1C)
- ✅ Implemented Material Design 3 theming
- ✅ Created dark/light mode support
- ✅ Designed all custom dialog layouts
- ✅ Created vector icon library (20+ icons)
- ✅ Implemented responsive layouts for different screen sizes
- ✅ Designed empty state components
- ✅ Created card and button styling system

#### Code Snippets:
```xml
<!-- colors.xml - Color System -->
<color name="authority_blue">#1E3A8A</color>
<color name="profit_green">#15803D</color>
<color name="controlled_red">#B91C1C</color>
```

---

### 👤 Member 2 - Backend Lead (Database & Logic)

**Responsibilities:** Room Database, DAOs, Entities, Repositories, ViewModels

#### Files Owned:
```
java/com/example/budgetboss/
├── data/
│   ├── local/
│   │   ├── database/
│   │   │   └── AppDatabase.java        # Room database setup
│   │   ├── dao/
│   │   │   ├── TransactionDao.java     # Transaction queries
│   │   │   ├── BudgetGoalDao.java      # Budget queries
│   │   │   ├── VaultDao.java           # Vault queries
│   │   │   └── InvestmentDao.java      # Investment queries
│   │   └── entity/
│   │       ├── TransactionEntity.java  # Transaction model
│   │       ├── BudgetGoalEntity.java   # Budget model
│   │       ├── VaultEntity.java        # Vault model
│   │       └── InvestmentEntity.java   # Investment model
│   └── repository/
│       ├── TransactionRepository.java  # Transaction data ops
│       ├── BudgetRepository.java       # Budget data ops
│       ├── VaultRepository.java        # Vault data ops
│       └── InvestmentRepository.java   # Investment data ops
├── di/
│   ├── AppModule.java                  # Hilt app module
│   └── DatabaseModule.java             # Database DI module
└── domain/
    └── models/
        ├── Transaction.java            # Domain transaction
        ├── Budget.java                 # Domain budget
        └── Investment.java             # Domain investment
```

#### Key Contributions:
- ✅ Designed database schema with Room
- ✅ Implemented CRUD operations for all entities
- ✅ Created repository pattern for data abstraction
- ✅ Set up Hilt dependency injection
- ✅ Implemented LiveData for reactive data flow
- ✅ Created database migrations
- ✅ Designed Investment.InvestmentType enum (SIP, STOCK, MUTUAL_FUND, LOAN_GIVEN, LOAN_TAKEN, FD, OTHER)
- ✅ Implemented data validation logic

#### Code Snippets:
```java
// TransactionDao.java - Database Queries
@Dao
public interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<TransactionEntity>> getAllTransactions();
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'INCOME'")
    LiveData<Double> getTotalIncome();
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE'")
    LiveData<Double> getTotalExpense();
    
    @Insert
    void insert(TransactionEntity transaction);
    
    @Delete
    void delete(TransactionEntity transaction);
}
```

```java
// Investment.java - Investment Type Enum
public enum InvestmentType {
    SIP,
    STOCK,
    MUTUAL_FUND,
    LOAN_GIVEN,
    LOAN_TAKEN,
    FD,
    OTHER
}
```

---

### 👤 Member 3 - Features Developer (Core Modules)

**Responsibilities:** Transaction, Budget, Vault, Investments Fragments & ViewModels

#### Files Owned:
```
java/com/example/budgetboss/presentation/
├── transactions/
│   ├── TransactionsFragment.java       # Transaction list screen
│   ├── AddTransactionFragment.java     # Add/Edit transaction
│   └── TransactionViewModel.java       # Transaction state
├── budget/
│   ├── BudgetFragment.java             # Budget goals screen
│   ├── BudgetAdapter.java              # Budget list adapter
│   └── BudgetViewModel.java            # Budget state
├── vault/
│   ├── VaultFragment.java              # Vault screen
│   └── VaultViewModel.java             # Vault state
└── investments/
    ├── InvestmentsFragment.java        # Investments screen
    ├── InvestmentAdapter.java          # Investment list adapter
    └── InvestmentViewModel.java        # Investment state

res/layout/
├── fragment_transactions.xml           # Transactions layout
├── fragment_add_transaction.xml        # Add transaction layout
├── fragment_budget.xml                 # Budget layout
├── fragment_vault.xml                  # Vault layout
├── fragment_investments.xml            # Investments layout
├── dialog_add_budget.xml               # Add budget dialog
├── dialog_vault_transaction.xml        # Vault add/withdraw dialog
├── dialog_add_investment.xml           # Add investment dialog
├── dialog_investment_options.xml       # Investment options dialog
└── dialog_confirm_delete.xml           # Delete confirmation
```

#### Key Contributions:
- ✅ Implemented transaction add/edit/delete functionality
- ✅ Created budget goal management with progress tracking
- ✅ Built secure vault deposit/withdraw system
- ✅ Developed investment & loan tracking module
- ✅ Implemented custom dialogs with proper sizing
- ✅ Added Cash/UPI payment method options
- ✅ Created ChipGroup selection for investment types
- ✅ Implemented empty state handling
- ✅ Added search and filter functionality

#### Code Snippets:
```java
// VaultFragment.java - Vault Transaction Dialog
private void showVaultTransactionDialog(boolean isDeposit) {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_vault_transaction, null);
    builder.setView(dialogView);
    
    AlertDialog dialog = builder.create();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    
    // IMPORTANT: setLayout must be called AFTER show()
    dialog.getWindow().setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    );
}
```

```java
// InvestmentsFragment.java - Investment Type Selection
ChipGroup chipGroup = dialogView.findViewById(R.id.chipGroupType);
chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
    int chipId = checkedIds.get(0);
    if (chipId == R.id.chipSip) selectedType = Investment.InvestmentType.SIP;
    else if (chipId == R.id.chipStock) selectedType = Investment.InvestmentType.STOCK;
    else if (chipId == R.id.chipMutualFund) selectedType = Investment.InvestmentType.MUTUAL_FUND;
    else if (chipId == R.id.chipLoanGiven) selectedType = Investment.InvestmentType.LOAN_GIVEN;
    else if (chipId == R.id.chipLoanTaken) selectedType = Investment.InvestmentType.LOAN_TAKEN;
});
```

---

### 👤 Member 4 - Integration Lead (Firebase, Camera, Testing)

**Responsibilities:** Authentication, Receipt Scanner, Navigation, Testing, Build Config

#### Files Owned:
```
java/com/example/budgetboss/
├── presentation/
│   ├── auth/
│   │   ├── LoginActivity.java          # User login
│   │   ├── RegisterActivity.java       # User registration
│   │   └── AuthViewModel.java          # Auth state
│   ├── dashboard/
│   │   ├── DashboardFragment.java      # Home dashboard
│   │   └── DashboardViewModel.java     # Dashboard state
│   ├── profile/
│   │   ├── ProfileFragment.java        # User profile
│   │   └── ProfileViewModel.java       # Profile state
│   ├── settings/
│   │   └── SettingsFragment.java       # App settings
│   └── scanner/
│       └── ReceiptScannerActivity.java # OCR receipt scan
├── MainActivity.java                    # Main activity + nav
└── BudgetBossApplication.java          # Hilt application

res/
├── navigation/
│   └── nav_graph.xml                   # Navigation graph
├── xml/
│   └── file_paths.xml                  # FileProvider paths
└── layout/
    ├── activity_login.xml              # Login screen
    ├── activity_register.xml           # Register screen
    ├── activity_receipt_scanner.xml    # Scanner screen
    └── dialog_about.xml                # About dialog

AndroidManifest.xml                      # App manifest
app/build.gradle                         # Dependencies & config
google-services.json                     # Firebase config
proguard-rules.pro                       # ProGuard rules
```

#### Key Contributions:
- ✅ Integrated Firebase Authentication (Email/Password)
- ✅ Implemented receipt scanner with CameraX
- ✅ Integrated ML Kit for OCR text recognition
- ✅ Set up Navigation Component with bottom nav
- ✅ Implemented keyboard handling (hide bottom nav)
- ✅ Created theme toggle with AppCompatDelegate
- ✅ Set up FileProvider for camera images
- ✅ Configured Gradle build with all dependencies
- ✅ Implemented About dialog
- ✅ Set up Hilt application class

#### Code Snippets:
```java
// ReceiptScannerActivity.java - OCR Processing
private void processImageForOcr(Uri imageUri) {
    InputImage image = InputImage.fromFilePath(this, imageUri);
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    
    recognizer.process(image)
        .addOnSuccessListener(text -> {
            String extractedText = text.getText();
            // Parse amount, date, merchant from text
            parseReceiptData(extractedText);
        })
        .addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to scan receipt", Toast.LENGTH_SHORT).show();
        });
}
```

```java
// MainActivity.java - Keyboard Visibility Handling
private void setupKeyboardListener() {
    View rootView = findViewById(android.R.id.content);
    rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = rootView.getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;
        
        if (keypadHeight > screenHeight * 0.15) {
            bottomNav.setVisibility(View.GONE);
        } else {
            bottomNav.setVisibility(View.VISIBLE);
        }
    });
}
```

```java
// SettingsFragment.java - Theme Toggle
switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
    if (isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
});
```

---

## 📊 Work Summary

| Module | Member 1 | Member 2 | Member 3 | Member 4 |
|--------|:--------:|:--------:|:--------:|:--------:|
| **UI/Themes** | ✅ Lead | - | - | - |
| **Database** | - | ✅ Lead | - | - |
| **Transactions** | Layout | DAO/Repo | ✅ Lead | - |
| **Budget** | Layout | DAO/Repo | ✅ Lead | - |
| **Vault** | Layout | DAO/Repo | ✅ Lead | - |
| **Investments** | Layout | DAO/Repo | ✅ Lead | - |
| **Authentication** | Layout | - | - | ✅ Lead |
| **Dashboard** | Layout | - | - | ✅ Lead |
| **Profile/Settings** | Layout | - | - | ✅ Lead |
| **Receipt Scanner** | - | - | - | ✅ Lead |
| **Navigation** | - | - | - | ✅ Lead |
| **Build Config** | - | - | - | ✅ Lead |

### Lines of Code (Approximate)

| Member | Java Files | XML Files | Total LOC |
|--------|------------|-----------|-----------|
| Member 1 | 0 | 25+ | ~2,500 |
| Member 2 | 15+ | 0 | ~1,800 |
| Member 3 | 12+ | 10+ | ~2,200 |
| Member 4 | 10+ | 5+ | ~1,500 |
| **Total** | **37+** | **40+** | **~8,000** |

---

## 🔄 Collaboration Points

### Shared Interfaces
1. **Entity ↔ Layout**: Member 2 defines data structure, Member 1/3 create UI
2. **ViewModel ↔ Fragment**: Member 2 provides data, Member 3 consumes it
3. **Navigation ↔ Fragments**: Member 4 sets up nav, Member 3 implements destinations
4. **Theme ↔ All Layouts**: Member 1 defines colors/styles, everyone uses them

### Code Reviews Required
- Database schema changes → All members
- Navigation changes → Member 3, Member 4
- Theme/Color changes → Member 1, Member 3
- Auth flow changes → Member 4, Member 3

---

<p align="center">
  <strong>BudgetBoss</strong> - Your trusted partner in financial management
</p>

<p align="center">
  Made with ❤️ by the BudgetBoss Team
</p>
