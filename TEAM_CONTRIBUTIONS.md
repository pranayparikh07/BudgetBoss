# BudgetBoss — How Pranay, Raj, Shreya, and Vishwa Built the App (Wireframes + UI + Engineering)

This document gives a long, detailed, end‑to‑end narrative of how four collaborators — **Pranay** (Product & UX), **Raj** (Backend & Architecture), **Shreya** (UI Engineering), and **Vishwa** (Integrations & QA) — conceived, designed (wireframes + high‑fidelity UI), and engineered the **BudgetBoss** Android application.

---

## 1) Vision, Principles, and Goals

- **Vision:** Help users take control of personal finances with clarity, confidence, and simplicity.
- **Design Principles:** Clarity first, reduce friction, consistent patterns, strong visual hierarchy, and accessible contrast.
- **Engineering Principles:** MVVM architecture, single source of truth, testable units, DI with Hilt, and clean separation between UI, domain, and data layers.

---

## 2) Roles and Responsibilities (Who Did What)

### Pranay — Product & UX Lead
- Drove product requirements, success metrics, and user journeys.
- Produced low‑fidelity **wireframes** (paper + Figma) for all core flows: Onboarding/Intro, Dashboard, Add Transaction, Transactions, Budget, Vault, Investments, Profile, Settings.
- Authored **UX copy** (labels, empty states, errors, hints) and dialog acceptance criteria.
- Owned information architecture and navigation map (bottom nav + deep links).
- Coordinated design critiques, incorporated feedback, and maintained a backlog of small UX improvements.

### Raj — Backend & Architecture Lead
- Defined the **MVVM + Repository** architecture, designed **Room** schema (entities, DAOs), and wrote migrations.
- Implemented business logic for budgets (limits and progress), vault operations (add/withdraw, payment method handling), transactions (income/expense categorization), and investments (SIP/Stock/Mutual Fund/Fd/Loan Given/Loan Taken).
- Built **Hilt modules** and lifecycle‑aware **ViewModels**; ensured reactive data with LiveData/Flow.
- Established validation rules, error states, and guards for safe data operations.
- Ensured performance (indexing, efficient queries), reliability, and offline‑first behavior.

### Shreya — UI Engineer (Design Systems & Components)
- Created the **Material 3 design system** with brand palette: Authority Blue (#1E3A8A), Profit Green (#15803D), Controlled Red (#B91C1C).
- Delivered **tokens** (`textPrimary`, `textSecondary`, `divider`, `income`, `expense`, `savings`) and typography, shapes, and component styles.
- Built reusable XML layouts and components: cards, list items, empty states, chips, custom dialogs (with proper sizing and dim backgrounds).
- Implemented **light/dark themes** with AA contrast and responsive layouts.
- Produced the **icon library** (vector drawables) aligned with brand aesthetics.

### Vishwa — Integrations & QA
- Integrated **Firebase Auth**, **CameraX + ML Kit OCR**, **FileProvider**, and the **Navigation Component**.
- Implemented the **first‑launch intro** (chalkboard animation with three words: TRACK → SAVE → GROW), splash flow, and bottom‑nav keyboard handling.
- Wrote smoke tests, manual QA checklists, and verified ProGuard/R8 configurations.
- Managed build pipelines, device compatibility checks, and release readiness.

---

## Wireframes & Figma UI

This section focuses only on the wireframing and visual UI work completed in Figma, from ideation to developer‑ready specs.

### Low‑Fidelity Wireframes (Pranay)
- Rapid sketches of core flows to validate navigation and content hierarchy:
	- First‑launch Intro (TRACK → SAVE → GROW) → Splash → Dashboard
	- Add Transaction (Income/Expense) with receipt scan entry point
	- Budget goal creation (limit, period: Weekly/Monthly/Yearly, progress)
	- Vault operations (Add/Withdraw with Cash/UPI options)
	- Investments (type chips: SIP, Stock, Mutual Fund, FD, Loans)
- Defined empty states and error fallback screens to ensure graceful UX.

### Mid‑Fidelity Figma Frames (Pranay + Shreya)
- Converted sketches into structured frames with spacing, grid, and alignment.
- Annotated transitions, dialog sizes, and responsive behaviors.
- Documented interaction rules (e.g., dialog sizing must be applied after show).
- Finalized copy for labels, hints, errors, and CTAs.

### High‑Fidelity Figma UI (Shreya)
- Applied the design system (Authority Blue #1E3A8A, Profit Green #15803D, Controlled Red #B91C1C).
- Delivered component specs: cards, list items, chips, dialogs (radius, elevation, padding, dim amount).
- Provided variant states: default, hover/focus, selected, error, disabled.
- Ensured AA contrast compliance; set minimum tap target sizes.
- Exported developer‑ready redlines for margins, font sizes, colors, and spacing.

### Screen‑by‑Screen UI Summary
- Intro (first launch only): Chalkboard backdrop, animated chalk words, fade to app.
- Dashboard: Balance overview, income/expense stats, recent list, quick actions.
- Add Transaction: Type toggle, amount, category, date, notes, receipt scan.
- Transactions List: Search/filter, date grouping, swipe edit/delete.
- Budget: Progress bars, limit vs spent, period badges, add dialog, empty states.
- Vault: Balance, add/withdraw, payment chips (Cash/UPI), history.
- Investments: Type chips (SIP/Stock/Mutual Fund/FD/Loans), amount/rate, calculators.
- Profile/Settings: Avatar, info, theme toggle, about, sign out.

### Figma Handoff Artifacts
- Component library with tokens (colors, typography, spacing) and states.
- Screen redlines: paddings, sizes, radii, elevation, and color references.
- Interaction notes: transitions, keyboard behaviors, dialog presentation rules.

---

## 4) UI Implementation: Mapping Wireframes → Screens

- **Intro (first launch only):** Chalkboard background, chalk‑style font, animated sequence of words (TRACK → SAVE → GROW), transitions to main.
- **Dashboard:** Balance overview, income/expense cards, recent transactions list, quick actions.
- **Add Transaction:** Income/Expense toggle, amount + category + date, receipt scan button, notes field.
- **Transactions List:** Search, filter by type/category, grouped by date, swipe to edit/delete.
- **Budget:** Cards with progress bars, limit vs spent, period badges (Weekly/Monthly/Yearly), add budget dialog, empty states.
- **Vault:** Large balance display, add/withdraw actions, payment method chips (Cash/UPI), transaction history.
- **Investments:** Type chips (SIP/Stock/Mutual Fund/FD/Loans), amount/rate inputs, return/interest calculators, list of entries.
- **Profile & Settings:** Avatar, user info, theme toggle, about dialog, sign out.

Key Implementation Notes:
- Dialog sizing fixed by calling `dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT)` **after** `dialog.show()` in fragments.
- Keyboard handling hides bottom nav during input for clear forms.
- Consistent component spacing and readable contrast across themes.

---

## Code: Architecture & Implementation

This section focuses only on the code: structure, modules, patterns, and critical implementation details.

### Architecture Overview
- **Pattern:** MVVM + Repository with clear separation of concerns.
- **Layers:**
	- Presentation: Activities/Fragments + Adapters
	- ViewModels: Lifecycle‑aware state + business orchestration
	- Data: Repositories (Room, Firebase, local services)
	- Storage: Room entities/DAOs and migrations
- **DI:** Hilt modules wire dependencies across layers.

### Data Layer (Room)
- **Entities:** `TransactionEntity`, `BudgetGoalEntity`, `VaultEntity`, `InvestmentEntity`.
- **DAOs:**
	- Transactions: CRUD, totals (income/expense), date‑grouped queries
	- Budgets: CRUD, progress calculations
	- Vault: balance ops, history
	- Investments: CRUD, type filtering (SIP/Stock/Mutual Fund/FD/Loans)
- **Repositories:** Wrap DAOs, enforce validation, expose LiveData/Flow to ViewModels.

### ViewModels
- `TransactionViewModel`: Add/edit/delete, totals, receipt OCR pre‑fill handling.
- `BudgetViewModel`: Create budgets, compute usage, overspend warnings.
- `VaultViewModel`: Add/withdraw funds, payment method chips, balance updates.
- `InvestmentViewModel`: Track investments/loans, compute returns/interest.
- `DashboardViewModel`: Aggregate KPIs for the home dashboard.

### Presentation Layer
- **Fragments/Activities:** Implement screens mapped from Figma specs.
- **Adapters:** Efficient lists for transactions and investments.
- **Dialogs:** MaterialCard‑wrapped custom dialogs with 28dp radius and dim amount.
- **Keyboard Handling:** Hide bottom nav on input to avoid occlusion.

### Critical Implementation Notes
- **Dialog Sizing:** Always call `dialog.show()` first, then `dialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT)`.
- **Intro Screen (First Launch):** SharedPreferences flag (`intro_shown`) ensures one‑time display.
- **Chalk Font:** Use local `permanent_marker.ttf` via `res/font` to avoid runtime certificate issues.
- **OCR Integration:** CameraX + ML Kit Text Recognition parses receipts; gracefully handle failures.
- **Navigation:** SafeArgs, tidy graph, predictable transitions.

### Build & Config
- **Gradle:** Material3, Lifecycle, Navigation, Room, Hilt, Firebase BOM, ML Kit, MPAndroidChart.
- **ProGuard/R8:** Rules for safe shrinking; verify OCR and Hilt.
- **AndroidManifest:** Launcher points to IntroActivity; FileProvider and permissions configured.


---

## 6) Integrations & Quality (Vishwa)

- **Firebase Authentication:** Email/password sign‑in with auth state observation.
- **CameraX + ML Kit OCR:** Capture receipts → extract text → parse likely amount/date/merchant.
- **FileProvider:** Secure image sharing and storage for receipts.
- **Navigation Component:** SafeArgs, graph setup, and controlled transitions.
- **Intro Screen Logic:** SharedPreferences flag to ensure first‑launch intro shows only once.
- **QA & Release:** Device matrix checks, smoke tests, build verification, and crash log triage.

---

## 7) Design System Details (Shreya)

- **Palette:** Authority Blue (#1E3A8A), Profit Green (#15803D), Controlled Red (#B91C1C), with supporting blues/greens/reds for light/dark variants.
- **Tokens:** `textPrimary`, `textSecondary`, `divider`, `income`, `expense`, `savings`.
- **Components:** MaterialCardView, ChipGroup, dialogs (rounded corners, dim backgrounds), list items, progress bars, selectors.
- **Accessibility:** AA contrast, minimum text sizes, input spacing, and clear focus states.
- **Themes:** Day/Night with harmonized status/navigation bars and surface variants.

---

## 8) Collaboration Workflow (All)

1. **Planning:** Weekly sprints; backlog tagged by owner (UX, Backend, UI, Integrations).
2. **Design‑First:** Wireframes → design review → hi‑fi spec → UI implementation.
3. **Engineering Sequence:** Data layer first (Room/Repos) → ViewModels → Fragments → Integrations.
4. **Reviews:** PRs reviewed cross‑functionally; UX and architecture sign‑offs for risky changes.
5. **Validation:** Build checks, device testing, and UX acceptance criteria before merge.

Artifacts Maintained:
- Figma files (wireframes + UI specs), architecture diagrams, API/DB schema, QA checklists.

---

## 9) Timeline (Expanded)

- **Week 1:** Requirements gathering, low‑fi wireframes, IA, brand palette, tokens.
- **Week 2:** Room schema/DAOs/Repos, Hilt modules, nav graph, base screens.
- **Week 3:** Dialogs and flows for Transactions, Budget, Vault, Investments; theme polish.
- **Week 4:** Firebase Auth, CameraX + OCR, first‑launch intro, keyboard handling; QA passes.
- **Week 5 (Buffer):** Bug fixes, performance tuning, accessibility adjustments, release prep.

---

## 10) Ownership Map

- **Pranay:** Wireframes, UX copy, flows, acceptance criteria.
- **Raj:** Room/DAOs/Repos, ViewModels, business rules, validation.
- **Shreya:** Layouts, styles, themes, drawables, dialogs, empty states.
- **Vishwa:** Firebase/Auth, Camera/OCR, Navigation, intro screen, QA & builds.

---

## 11) Lessons Learned & Key Decisions

- Dialog window sizing must be applied **after** `show()` to avoid vertical text/layout issues.
- One‑time intro experience creates delight without adding friction; use SharedPreferences flag.
- OCR boosts speed for adding transactions but requires graceful fallbacks and manual editing.
- Consistent component library accelerates delivery and guarantees visual coherence.

---

## 12) Final Notes

BudgetBoss is the result of tight collaboration across product, design, and engineering. The team balanced aesthetics (Authority Blue, Profit Green, Controlled Red) with robust architecture (MVVM, Room, Hilt), delivering a finance app that feels both **professional** and **friendly**, with thoughtful UX touches like empty states, polished dialogs, and a memorable first‑launch intro.
