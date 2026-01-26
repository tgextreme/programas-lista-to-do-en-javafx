@echo off
REM ========================================================================
REM Script para compilar usando Maven de Eclipse
REM Ubicacion de Eclipse: C:\Users\usuario\eclipse\java-2025-06
REM ========================================================================

echo.
echo ========================================================================
echo     Compilar con Maven de Eclipse
echo ========================================================================
echo.

set ECLIPSE_HOME=C:\Users\usuario\eclipse\java-2025-06
set PROJECT_DIR=%~dp0

echo Buscando Maven en Eclipse...
echo Eclipse: %ECLIPSE_HOME%
echo Proyecto: %PROJECT_DIR%
echo.

REM Buscar Maven en los plugins de Eclipse
set MAVEN_CMD=
for /d %%d in ("%ECLIPSE_HOME%\plugins\org.eclipse.m2e.maven.runtime_*") do (
    if exist "%%d\maven\bin\mvn.cmd" (
        set MAVEN_CMD=%%d\maven\bin\mvn.cmd
        echo Maven encontrado: !MAVEN_CMD!
        goto :found_maven
    )
)

echo Maven no encontrado en plugins de Eclipse
echo.
echo Intentando usar Maven del sistema...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Maven no encontrado
    echo.
    echo Por favor, compila manualmente en Eclipse:
    echo   1. Abre Eclipse
    echo   2. Click derecho en "todo.crud.list"
    echo   3. Run As -^> Maven build...
    echo   4. Goals: clean package
    echo   5. Run
    pause
    exit /b 1
)

set MAVEN_CMD=mvn
echo Usando Maven del sistema

:found_maven
echo.
echo ========================================================================
echo     Compilando proyecto...
echo ========================================================================
echo.

cd "%PROJECT_DIR%"

"%MAVEN_CMD%" clean package -DskipTests

if errorlevel 1 (
    echo.
    echo ERROR: Compilacion fallida
    echo Por favor revisa los errores arriba
    pause
    exit /b 1
)

echo.
echo ========================================================================
echo     COMPILACION EXITOSA
echo ========================================================================
echo.

if exist "target\todo.crud.list-0.0.1-SNAPSHOT.jar" (
    echo JAR creado: target\todo.crud.list-0.0.1-SNAPSHOT.jar
    echo.
    echo Ahora puedes ejecutar:
    echo   python create_installer.py
    echo.
) else (
    echo ADVERTENCIA: JAR no encontrado
    echo Por favor verifica la compilacion
    echo.
)

pause
