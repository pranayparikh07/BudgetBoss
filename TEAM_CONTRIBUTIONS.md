# BudgetBoss — Team Contributions and Process

This document explains how four team members — Pranay, Raj, Shreya, and Vishwa — collaboratively designed and built the BudgetBoss app, including wireframes and final UI.

---

## Team Roles

### Pranay — Product & UX Lead
- Defined core user journeys: onboarding, dashboard, transactions, budgets, vault, investments, profile
- Led wireframing in low-fidelity (paper + Figma) to validate flows quickly
- Owned information architecture, navigation structure, and empty states
- Wrote acceptance criteria and UX copy for dialogs, errors, and prompts
- Coordinated user feedback loops and iteration cadence

### Raj — Backend & Architecture Lead
- Established MVVM architecture with Repository pattern and Room database
- Implemented DAOs, entities, migrations, and data validation rules
- Set up Hilt DI modules and lifecycle-aware ViewModels
- Built business logic for budgets, vault transactions, and investments/loans
- Ensured performance, consistency, and offline-first behavior

### Shreya — UI Engineer (Design Systems & Components)
- Created Material 3 design system: color tokens, typography, shapes
- Built reusable XML components (cards, list items, empty states, dialogs)
- Implemented light/dark themes and ensured AA contrast ratios
- Delivered high-fidelity UI matching wireframes, with responsive layouts
- Produced icon set and vector drawables aligned with the brand palette

### Vishwa — Integrations & QA
- Integrated Firebase Auth, Camera + ML Kit OCR, FileProvider
- Configured Navigation Component, splash/intro, and keyboard handling
- Wrote smoke tests and manual QA checklists for critical flows
- Automated debug builds and verified ProGuard/R8 configs
- Owned release readiness and device compatibility checks

---

## Collaboration Workflow

- Plan: Weekly sprint planning with a shared backlog in issues
- Design First: Wireframes → design reviews → hi-fi specs (Figma)
- Dev Sequencing: Backend APIs/entities first → fragments/UI → integrations
- Reviews: PR reviews by cross-functional owners (UI ↔ Backend ↔ Integrations)
- Validation: Build verification, device testing, and UX acceptance criteria

---

## Wireframing Process

- Low-Fidelity: Paper sketches of dashboard, transactions, budgets, vault, investments
- Mid-Fidelity: Figma frames for navigation flow and dialogs
- High-Fidelity: Component library applied (cards, chips, dialogs, typography)
- Usability Checks: Content hierarchy, input flows, error states, and empty states

Key wireframes covered:
- Onboarding intro (three chalk words) → splash → home
- Dashboard metrics with quick actions
- Add transaction: type toggle, amount, category, date, notes, receipt scan
- Budget creation dialog with period selection
- Vault add/withdraw with payment method (Cash/UPI)
- Investments with type chip selection (SIP, Stock, Mutual Fund, Loans)

---

## UI Design System

- Palette: Authority Blue (#1E3A8A), Profit Green (#15803D), Controlled Red (#B91C1C)
- Tokens: textPrimary, textSecondary, divider, income, expense, savings
- Components: MaterialCard, ChipGroup, dialogs, list items, progress bars
- Accessibility: Minimum contrast AA, large target sizes, consistent focus/hover
- Themes: Day/Night with surface variants and status-bar/nav-bar harmonization

---

## Build & Feature Ownership Map

- Pranay: Wireframes, UX copy, flows, acceptance criteria
- Raj: Data layer (Room, DAOs, Repositories), ViewModels, validation
- Shreya: Layouts, styles, themes, drawables, dialogs, empty states
- Vishwa: Firebase, Camera/OCR, Navigation, intro screen, QA & builds

---

## Timeline (Simplified)

1. Week 1: Requirements, low-fi wireframes, IA, design tokens
2. Week 2: Room schema, repositories, Hilt modules, navigation graph
3. Week 3: Screens and dialogs (transactions, budgets, vault, investments)
4. Week 4: Integrations (Auth, OCR), intro animation, polishing, QA

---

## Review & Sign-off

- UX sign-off: Pranay + Shreya
- Architecture sign-off: Raj
- Release sign-off: Vishwa

---

## Notes

- Intro screen shows only on first launch and transitions to app
- Dialog sizing fixed by calling `setLayout()` after `show()` in fragments
- Receipt scanner extracts text to pre-fill transaction fields

The combined effort ensured a polished, performant, and user-friendly BudgetBoss experience.
