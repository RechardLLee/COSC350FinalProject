@echo off
echo Cleaning up project files...

:: 删除旧的编译和临时目录
rmdir /s /q "bin" 2>nul
rmdir /s /q "compile" 2>nul
rmdir /s /q "temp" 2>nul

:: 删除重复的目录
rmdir /s /q "Database Related" 2>nul
rmdir /s /q "Developer Tools" 2>nul
rmdir /s /q "Feedback System" 2>nul
rmdir /s /q "Game Functionality" 2>nul
rmdir /s /q "Main Interfaces" 2>nul
rmdir /s /q "User Management" 2>nul
rmdir /s /q "Utility Classes" 2>nul

:: 删除重复的游戏文件
del /f /q "game\roullete.java" 2>nul
del /f /q "game\guess_number_game.py" 2>nul

:: 删除其他不需要的文件
del /f /q "GamePlatform\game\*.java" 2>nul

:: 创建必要的目录结构
mkdir "compile" 2>nul
mkdir "compile\GamePlatform\Game" 2>nul
mkdir "src\GamePlatform\Game" 2>nul

echo Creating new directory structure...

:: 确保基本目录结构存在
mkdir "src\GamePlatform\Database" 2>nul
mkdir "src\GamePlatform\Developer" 2>nul
mkdir "src\GamePlatform\Feedback" 2>nul
mkdir "src\GamePlatform\Game" 2>nul
mkdir "src\GamePlatform\Main\Interfaces" 2>nul
mkdir "src\GamePlatform\User\Management" 2>nul
mkdir "src\GamePlatform\Utility" 2>nul

echo Cleanup completed!
echo.
echo New directory structure:
echo GamePlatform/
echo ├── src/
echo │   └── GamePlatform/
echo │       ├── Database/
echo │       ├── Developer/
echo │       ├── Feedback/
echo │       ├── Game/
echo │       ├── Main/
echo │       ├── User/
echo │       └── Utility/
echo ├── game/
echo ├── lib/
echo ├── compile/
echo └── run.bat
echo.
pause 