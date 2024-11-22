@echo off
:: Set UTF-8 encoding
chcp 65001

:: Create bin directory and package structure
if not exist "bin\GamePlatform\Database" mkdir "bin\GamePlatform\Database"
if not exist "bin\GamePlatform\Developer" mkdir "bin\GamePlatform\Developer"
if not exist "bin\GamePlatform\Feedback" mkdir "bin\GamePlatform\Feedback"
if not exist "bin\GamePlatform\Game" mkdir "bin\GamePlatform\Game"
if not exist "bin\GamePlatform\Main\Interfaces" mkdir "bin\GamePlatform\Main\Interfaces"
if not exist "bin\GamePlatform\User\Management" mkdir "bin\GamePlatform\User\Management"
if not exist "bin\GamePlatform\Utility" mkdir "bin\GamePlatform\Utility"

:: Create lib directory if it doesn't exist
if not exist "lib" mkdir "lib"

:: Set CLASSPATH with SQLite JDBC driver
set JDBC_PATH="lib\sqlite-jdbc-3.47.0.0.jar"

:: Check if JDBC driver exists
if not exist %JDBC_PATH% (
    echo SQLite JDBC driver not found at %JDBC_PATH%
    echo Please download it from https://github.com/xerial/sqlite-jdbc/releases
    pause
    exit /b 1
)

:: Set CLASSPATH
set CLASSPATH=.;bin;%JDBC_PATH%

:: Create source directories if they don't exist
if not exist "src\GamePlatform\Database" mkdir "src\GamePlatform\Database"
if not exist "src\GamePlatform\Developer" mkdir "src\GamePlatform\Developer"
if not exist "src\GamePlatform\Feedback" mkdir "src\GamePlatform\Feedback"
if not exist "src\GamePlatform\Game" mkdir "src\GamePlatform\Game"
if not exist "src\GamePlatform\Main\Interfaces" mkdir "src\GamePlatform\Main\Interfaces"
if not exist "src\GamePlatform\User\Management" mkdir "src\GamePlatform\User\Management"
if not exist "src\GamePlatform\Utility" mkdir "src\GamePlatform\Utility"

:: Copy source files to correct package structure
xcopy /y "Database Related\*.java" "src\GamePlatform\Database\"
xcopy /y "Developer Tools\*.java" "src\GamePlatform\Developer\"
xcopy /y "Feedback System\*.java" "src\GamePlatform\Feedback\"
xcopy /y "Game Functionality\*.java" "src\GamePlatform\Game\"
xcopy /y "Main Interfaces\*.java" "src\GamePlatform\Main\Interfaces\"
xcopy /y "User Management\*.java" "src\GamePlatform\User\Management\"
xcopy /y "Utility Classes\*.java" "src\GamePlatform\Utility\"

:: Copy FXML files to bin directory
xcopy /s /y "User Management\*.fxml" "bin\GamePlatform\User\Management\"
xcopy /s /y "Main Interfaces\*.fxml" "bin\GamePlatform\Main\Interfaces\"
xcopy /s /y "Developer Tools\*.fxml" "bin\GamePlatform\Developer\"
xcopy /s /y "Feedback System\*.fxml" "bin\GamePlatform\Feedback\"

:: Compile all Java files from src directory
javac -encoding UTF-8 -d bin -cp "%CLASSPATH%" ^
    src\GamePlatform\Database\*.java ^
    src\GamePlatform\Developer\*.java ^
    src\GamePlatform\Feedback\*.java ^
    src\GamePlatform\Game\*.java ^
    src\GamePlatform\Main\Interfaces\*.java ^
    src\GamePlatform\User\Management\*.java ^
    src\GamePlatform\Utility\*.java

:: Run the program if compilation succeeds
if %errorlevel% == 0 (
    echo Compilation successful, starting program...
    java -cp "%CLASSPATH%" GamePlatform.Main.Interfaces.GamePlatformApp
) else (
    echo Compilation failed, check error messages
    pause
)

pause 