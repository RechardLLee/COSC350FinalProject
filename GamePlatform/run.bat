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

:: Create necessary directories
mkdir "compile\GamePlatform\User\Management" 2>nul
mkdir "compile\GamePlatform\Developer" 2>nul
mkdir "compile\GamePlatform\Feedback" 2>nul
mkdir "compile\GamePlatform\Main\Interfaces" 2>nul
mkdir "compile\GamePlatform\Game" 2>nul
mkdir "compile\GamePlatform\Game\BlackJackGui" 2>nul
mkdir "compile\GamePlatform\Game\bingo" 2>nul
mkdir "compile\GamePlatform\Game\GoFish" 2>nul

:: Copy FXML files
xcopy /s /y "src\GamePlatform\User\Management\*.fxml" "compile\GamePlatform\User\Management\"
xcopy /s /y "src\GamePlatform\Developer\*.fxml" "compile\GamePlatform\Developer\"
xcopy /s /y "src\GamePlatform\Feedback\*.fxml" "compile\GamePlatform\Feedback\"
xcopy /s /y "src\GamePlatform\Main\Interfaces\*.fxml" "compile\GamePlatform\Main\Interfaces\"
xcopy /s /y "src\GamePlatform\Game\bingo\*.fxml" "compile\GamePlatform\Game\bingo\"
xcopy /s /y "src\GamePlatform\Game\GoFish\*.fxml" "compile\GamePlatform\Game\GoFish\"

:: Copy card images
mkdir "compile\GamePlatform\Game\BlackJackGui\cards" 2>nul
xcopy /s /y "src\GamePlatform\Game\BlackJackGui\cards\*.*" "compile\GamePlatform\Game\BlackJackGui\cards\"
xcopy /s /y "src\GamePlatform\Game\BlackJackGui\BlackJack.png" "compile\GamePlatform\Game\BlackJackGui\"
mkdir "compile\GamePlatform\Game\GoFish\cards" 2>nul
xcopy /s /y "src\GamePlatform\Game\GoFish\cards\*.png" "compile\GamePlatform\Game\GoFish\cards\"
xcopy /s /y "src\GamePlatform\Game\GoFish\card_back.png" "compile\GamePlatform\Game\GoFish\"
mkdir "compile\GamePlatform\Image" 2>nul
xcopy /s /y "src\GamePlatform\Image\*"

:: Compile in correct order
javac -encoding UTF-8 -d compile -sourcepath src ^
    src\GamePlatform\Game\GameStats.java ^
    src\GamePlatform\Game\GameRecord.java ^
    src\GamePlatform\Game\GameRecordManager.java ^
    src\GamePlatform\Game\BaseGame.java ^
    src\GamePlatform\Game\GoFish\Rank.java ^
    src\GamePlatform\Game\GoFish\Suit.java ^
    src\GamePlatform\Game\GoFish\Card.java ^
    src\GamePlatform\Game\GoFish\Deck.java ^
    src\GamePlatform\Game\GoFish\GoFishController.java ^
    src\GamePlatform\Game\GoFish\GoFish.java ^
    src\GamePlatform\Game\BlackJackGui\BlackJackGame.java ^
    src\GamePlatform\Game\BlackJack.java ^
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
    src\GamePlatform\User\Management\*.java

:: Run
if %errorlevel% == 0 (
    echo Compilation successful, starting program...
    java -cp "%CLASSPATH%" GamePlatform.Main.Interfaces.GamePlatformApp
) else (
    echo Compilation failed, check error messages
)

pause 