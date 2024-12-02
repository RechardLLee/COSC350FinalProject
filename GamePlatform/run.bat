@echo off
:: Set UTF-8 encoding
chcp 65001

:: Create compile directory
if not exist "compile" mkdir "compile"

:: Clean previous compilation
rmdir /s /q "compile" 2>nul
mkdir "compile"

:: Set CLASSPATH
set CLASSPATH=compile;^
lib\sqlite-jdbc-3.42.0.0.jar;^
lib\javax.mail.jar;^
lib\activation.jar

:: Create necessary directories for FXML files
mkdir "compile\GamePlatform\User\Management" 2>nul
mkdir "compile\GamePlatform\Developer" 2>nul
mkdir "compile\GamePlatform\Feedback" 2>nul
mkdir "compile\GamePlatform\Main\Interfaces" 2>nul

:: Copy FXML files first
xcopy /s /y "src\GamePlatform\User\Management\LoginView.fxml" "compile\GamePlatform\User\Management\"
xcopy /s /y "src\GamePlatform\User\Management\SignUp.fxml" "compile\GamePlatform\User\Management\"
xcopy /s /y "src\GamePlatform\Developer\*.fxml" "compile\GamePlatform\Developer\"
xcopy /s /y "src\GamePlatform\Feedback\*.fxml" "compile\GamePlatform\Feedback\"
xcopy /s /y "src\GamePlatform\Main\Interfaces\*.fxml" "compile\GamePlatform\Main\Interfaces\"

:: List FXML files in compile directory (for debugging)
echo Listing all FXML files in compile directory:
dir /s /b "compile\*.fxml"

:: Compile utility classes first
javac -encoding UTF-8 -d compile -sourcepath src ^
    src\GamePlatform\Utility\LanguageUtil.java ^
    src\GamePlatform\Utility\EmailUtil.java

:: Compile model classes
javac -encoding UTF-8 -d compile -sourcepath src ^
    src\GamePlatform\User\Management\UserData.java ^
    src\GamePlatform\Feedback\ReviewData.java ^
    src\GamePlatform\Feedback\BugData.java

:: Compile service classes
javac -encoding UTF-8 -d compile -sourcepath src -cp "%CLASSPATH%" ^
    src\GamePlatform\Database\DatabaseService.java ^
    src\GamePlatform\User\Management\UserSession.java

:: Compile game classes
javac -encoding UTF-8 -d compile -sourcepath src -cp "%CLASSPATH%" ^
    src\GamePlatform\Game\SnakeGame.java ^
    src\GamePlatform\Game\HanoiTowerGame.java ^
    src\GamePlatform\Game\GuessNumberGame.java ^
    src\GamePlatform\Game\MemoryGame.java ^
    src\GamePlatform\Game\SlotMachine.java ^
    src\GamePlatform\Game\RouletteGame.java ^
    src\GamePlatform\Game\TicTacToe.java ^
    src\GamePlatform\Game\GameLauncher.java

:: Compile remaining files
javac -encoding UTF-8 -d compile -sourcepath src -cp "%CLASSPATH%" ^
    src\GamePlatform\User\Management\*.java ^
    src\GamePlatform\Feedback\*.java ^
    src\GamePlatform\Developer\*.java ^
    src\GamePlatform\Main\Interfaces\*.java

:: Run
if %errorlevel% == 0 (
    echo Compilation successful, starting program...
    java -cp "%CLASSPATH%" GamePlatform.Main.Interfaces.GamePlatformApp
) else (
    echo Compilation failed, check error messages
)

pause 