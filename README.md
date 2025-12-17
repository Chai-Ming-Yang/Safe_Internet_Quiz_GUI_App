# 🌐 Safe Internet Learning Module App
A Java-based desktop application designed to promote safe internet practices through interactive learning modules, quizzes, and leaderboards.  
Developed as part of the **Java Programming course (TMF2954)** at **Universiti Malaysia Sarawak (UNIMAS)**.

> Note:
> This project was originally developed outside GitHub and later uploaded to this repository.
> The following contributors were part of the original development team and are credited for their valuable contributions.

## 👥 Contributors
* @jordanlcr (Lee Chong Ren) — Collaborator
* @LeafStardust — Collaborator
* @Vecrex — Collaborator
* Chai-Ming-Yang — Lead Developer & Project Manager

💡 This project demonstrates practical understanding of:
- Object-Oriented Programming (OOP) principles
- Graphical User Interface (GUI) development using Java Swing
- Event handling (mouse and keyboard events)
- Database management with SQLite
- Secure password handling with BCrypt (salted hashing)
- JSON file processing using Gson

---

## Features

### 🧠 Learning Modules
Interactive modules that teach safe internet usage concepts.

### 📝 Quizzes
Short assessments after each module to reinforce learning.

### 🏆 Leaderboard
Displays top scores to motivate continuous learning.

### 📱 Mobile-Inspired UI
Although implemented with Java Swing and used with a mouse, the UI layout mimics mobile app design for a modern, user-friendly experience.

### 🔐 Secure Authentication
- Passwords are hashed using BCrypt with salt before being stored.
- Credentials and user progress are saved in an SQLite database.

---

## ⚙️ Technologies Used

| Purpose | Technology |
|--------|------------|
| GUI Framework | Java Swing (javax.swing) |
| Password Hashing | BCrypt |
| Database | SQLite |
| JSON Parsing | Gson |
| Build / Run Script | PowerShell (build.ps1) |

---

## 🚀 Getting Started

### 1. ⬇️ Clone the Repository

    ```
    git clone https://github.com/yourusername/your-repo-name.git
    ```

---

## ▶️ Run the Application

### Option 1: Run Without VS Code (Terminal Only)

**Navigate to the project folder:**

1. Open File Explorer and go to the cloned project folder.
2. Copy the folder path (right-click the address bar → Copy as path).
3. Open Terminal and run:
    ```
    cd "C:\your\project\path\Safe Internet"
    ```

**Run the build script:**
    ```
    .\build.ps1
    ```
    This will compile and launch the application automatically.

---

### Option 2: Run With Visual Studio Code

1. Open the project folder in VS Code.
2. Install the Java Extension Pack if prompted.
3. The app’s entry point is:
    ```
    src/App/Main.java
    ```

**Run the build script:**
    ```
    .\build.ps1
    ```
    VS Code will handle compilation and execution.
