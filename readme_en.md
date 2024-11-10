# Game Platform

A multilingual game platform system developed with JavaFX, supporting both English and Chinese interfaces.

## Features

### 1. User System
- User Login/Registration
- Developer Login Portal
- Language Switch (English/Chinese)

### 2. Game Management
- Game Showcase (including Snake and other games)
- Game Purchase
- Game Download
- Game Launch

### 3. Feedback System
- Bug Report
- Game Reviews (1-5 stars rating)
- User Comments

## Technical Architecture

- Development Language: Java
- UI Framework: JavaFX
- Project Structure: MVC Pattern

### Main Components

1. **Controllers**
   - MainController: Main interface controller
   - GameViewController: Game view controller
   - SignUpController: Registration controller
   - ReviewController: Review controller
   - BugReportController: Bug report controller
   - DeveloperLoginController: Developer login controller

2. **Utility Classes**
   - LanguageUtil: Language utility class, manages English/Chinese switching

3. **View Files (FXML)**
   - MainView.fxml: Main interface
   - GameView.fxml: Game interface
   - SignUp.fxml: Registration interface
   - Review.fxml: Review interface
   - BugReport.fxml: Bug report interface
   - DeveloperLogin.fxml: Developer login interface

## Interface Preview

### Main Interface
- Top Toolbar: User login, registration, language switch
- Central Area: Game display grid
- Right Sidebar: Bug report, reviews, developer portal

### Game Interface
- Game Icon Display
- Game Description
- Buy/Download/Launch Buttons

## Running Instructions

### System Requirements
- JDK 8 or higher
- JavaFX runtime environment

### Launch Methods
1. Double-click `run.bat` in Windows environment
2. Or via command line: 