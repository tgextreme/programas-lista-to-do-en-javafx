@echo off
REM Launcher for TODO CRUD List
REM Usa javaw para no mostrar ventana de CMD

cd /d "%~dp0"

if not exist "target\todo.crud.list-0.0.1-SNAPSHOT.jar" (
    echo ERROR: No se encuentra el archivo JAR
    echo.
    echo Por favor, compila el proyecto primero:
    echo   1. Abre Eclipse
    echo   2. Click derecho en el proyecto
    echo   3. Run As -^> Maven build...
    echo   4. Goals: clean package
    echo.
    pause
    exit /b 1
)

REM Usar javaw en lugar de java para no mostrar ventana de CMD
start javaw -jar "target\todo.crud.list-0.0.1-SNAPSHOT.jar"
