@echo off
echo Compiling Java files...

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Collect all Java files except those in gui directories
setlocal enabledelayedexpansion
set files=

for /R src %%f in (*.java) do (
    REM Check if the path contains \gui\
    echo %%f | findstr /i "\\gui\\" >nul
    if errorlevel 1 (
        REM If not found, add to files list
        set files=!files! "%%f"
    )
)

REM Compile all files together
javac -d bin !files!

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    exit /b %ERRORLEVEL%
)

echo Running the program...
java -cp bin Main

pause
