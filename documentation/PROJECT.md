# Carolinian Events Management System

## 1. Project Overview

### Description

Carolinian Events Management System is a native Android application designed for the University of San Carlos community. It allows students to discover campus events, register for activities, bookmark events, and receive event information through a modern mobile experience. Organizers can create and manage events directly from the application.

This project is developed as the capstone requirement for the Mobile Application Development course.

---

## 2. Objectives

### Primary Objectives

- Provide a centralized platform for campus events.
- Allow students to discover and join events.
- Allow organizers to create and manage events.
- Provide a clean and modern Material 3 interface.
- Demonstrate modern Android development practices.

---

## 3. Target Users

### Student

Can:
- Register an account
- Login
- Browse events
- Search events
- View event details
- Bookmark events
- RSVP to events
- Manage profile

### Organizer

Can perform everything a Student can, plus:

- Create events
- Edit own events
- Delete own events
- View participants

---

## 4. MVP Features

### Authentication

- Login
- Register
- Logout

### Home

- Browse events
- Search events
- Filter by category

### Event Details

- View complete event information
- RSVP
- Bookmark

### Create Event

- Upload cover image
- Enter event details
- Select category
- Select date and time
- Select venue

### My Events

- View created events
- Edit events
- Delete events

### Bookmarks

- View saved events

### Profile

- Edit profile
- View joined events

---

## 5. Technology Stack

Language

- Kotlin

UI

- Jetpack Compose
- Material 3

Architecture

- MVVM
- Repository Pattern

Dependency Injection

- Hilt

Backend

- Firebase Authentication
- Cloud Firestore
- Firebase Storage

Local Storage

- Room Database

Navigation

- Navigation Compose

Image Loading

- Coil

Asynchronous Programming

- Kotlin Coroutines
- Flow

Version Control

- Git
- GitHub

---

## 6. Architecture

UI

↓

ViewModel

↓

Repository

↓

Data Sources

↓

Firestore

Room

Firebase Authentication

Firebase Storage

Rules

- UI never communicates directly with Firebase.
- ViewModels only communicate with repositories.
- Repositories manage local and remote data.
- Business logic belongs inside ViewModels and repositories.

---

## 7. Planned Screens

1. Splash

2. Login

3. Register

4. Home

5. Search

6. Event Details

7. Create Event

8. My Events

9. Bookmarks

10. Profile

---

## 8. Firestore Collections

users

events

bookmarks

registrations

---

## 9. Local Database (Room)

- Cached events
- Bookmarked events
- Recently viewed events

---

## 10. Project Structure

app/

data/

domain/

ui/

viewmodel/

di/

util/

---

## 11. Coding Standards

- Follow Kotlin Coding Conventions.
- Follow Google's Android Architecture Guide.
- Use SOLID principles.
- Keep functions small.
- Use descriptive naming.
- Avoid duplicated code.
- Prefer immutable state.
- Use StateFlow instead of LiveData.
- Use reusable composables.

---

## 12. Development Rules

- Build one feature at a time.
- Every feature must be commit-ready.
- Do not generate unnecessary code.
- Do not modify unrelated files.
- Keep the project modular.
- Explain architectural decisions before implementation.

---

## 13. Future Enhancements

- Push notifications
- QR code check-in
- Calendar integration
- Event comments
- Event ratings
- Maps integration
- Offline synchronization
- Dark mode customization

---

## 14. AI Instructions

When contributing to this project:

- Follow MVVM architecture.
- Use Repository Pattern.
- Use Jetpack Compose only.
- Use Material 3.
- Do not generate code for unrelated features.
- Work one milestone at a time.
- Wait for approval before proceeding to the next milestone.
- Explain implementation decisions.