@echo off
setlocal enabledelayedexpansion

set MODULE_PATH=lib/javafx-sdk-24.0.1/lib
set MODULES=javafx.controls
set OUTPUT_DIR=bin

if not exist %OUTPUT_DIR% (
    mkdir %OUTPUT_DIR%
)

:: Collect all .java files in src
set FILES=

for /r src %%f in (*.java) do (
    set FILES=!FILES! "%%f"
)

:: Compile all Java files
echo Compiling all Java files...
javac --module-path %MODULE_PATH% --add-modules %MODULES% -d %OUTPUT_DIR% %FILES%

if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

:: Copy all non-Java resources (like images) to bin
xcopy /E /Y /I src\gui\*.png %OUTPUT_DIR%\gui\
xcopy /E /Y /I src\gui\*.css %OUTPUT_DIR%\gui\

:: Run MainApp
echo Running MainApp...
java --module-path %MODULE_PATH% --add-modules %MODULES% -cp %OUTPUT_DIR% gui.MainApp

endlocal
