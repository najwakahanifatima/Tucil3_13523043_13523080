@echo off
echo Compiling Java files...

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Collect all Java files into a temp file list
setlocal enabledelayedexpansion
set files=

for /R src %%f in (*.java) do (
    set files=!files! "%%f"
)

REM Compile all files together
javac -d bin !files!

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    exit /b %ERRORLEVEL%
)

echo Running the program...
java -cp bin Main

@REM echo Deleting .class files...
@REM for /R bin %%f in (*.class) do del "%%f"

@REM echo Done.
pause
