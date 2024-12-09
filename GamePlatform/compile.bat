@echo off
:: Set UTF-8 encoding
chcp 65001 > nul

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

:: Create necessary directories
mkdir "compile\GamePlatform\User\Management" 2>nul
mkdir "compile\GamePlatform\Developer" 2>nul
mkdir "compile\GamePlatform\Feedback" 2>nul
mkdir "compile\GamePlatform\Main\Interfaces" 2>nul
mkdir "compile\GamePlatform\Game" 2>nul
mkdir "compile\GamePlatform\Game\bingo" 2>nul

:: Copy FXML files
xcopy /s /y "src\GamePlatform\User\Management\*.fxml" "compile\GamePlatform\User\Management\" > nul
xcopy /s /y "src\GamePlatform\Developer\*.fxml" "compile\GamePlatform\Developer\" > nul
xcopy /s /y "src\GamePlatform\Feedback\*.fxml" "compile\GamePlatform\Feedback\" > nul
xcopy /s /y "src\GamePlatform\Main\Interfaces\*.fxml" "compile\GamePlatform\Main\Interfaces\" > nul
xcopy /s /y "src\GamePlatform\Game\bingo\*.fxml" "compile\GamePlatform\Game\bingo\" > nul

:: Copy styles files
mkdir "compile\GamePlatform\Developer\styles" 2>nul
copy /y "src\GamePlatform\Developer\styles\styles.css" "compile\GamePlatform\Developer\styles\styles.css" > nul

:: Compile in correct order
javac -encoding UTF-8 -d compile -sourcepath src ^
    src\GamePlatform\Game\GameStats.java ^
    src\GamePlatform\Database\DatabaseService.java ^
    src\GamePlatform\User\Management\UserSession.java ^
    src\GamePlatform\Utility\*.java ^
    src\GamePlatform\User\Management\UserData.java ^
    src\GamePlatform\Feedback\*.java ^
    src\GamePlatform\Game\bingo\Bingo.java ^
    src\GamePlatform\Game\bingo\StandardBingo.java ^
    src\GamePlatform\Game\bingo\BingoController.java ^
    src\GamePlatform\Game\bingo\BingoRunner.java ^
    src\GamePlatform\Game\*.java ^
    src\GamePlatform\Developer\*.java ^
    src\GamePlatform\Main\Interfaces\*.java ^
    src\GamePlatform\User\Management\*.java > nul

:: Run
if %errorlevel% == 0 (
    start /wait javaw -cp "%CLASSPATH%" GamePlatform.Main.Interfaces.GamePlatformApp
    taskkill /F /IM javaw.exe > nul 2>&1
) else (
    echo Compilation failed > error.log
) 