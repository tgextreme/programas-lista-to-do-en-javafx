@echo off
REM ========================================================================
REM Script para compilar en Eclipse y crear instalador
REM ========================================================================

echo.
echo ========================================================================
echo     Compilar y Crear Instalador - TODO CRUD List
echo ========================================================================
echo.

REM Verificar si el JAR ya existe
if exist "target\todo.crud.list-0.0.1-SNAPSHOT.jar" (
    echo OK: JAR ya compilado encontrado
    echo Archivo: target\todo.crud.list-0.0.1-SNAPSHOT.jar
    echo.
    goto :run_script
)

echo AVISO: JAR no encontrado, necesita compilarse primero
echo.
echo ========================================================================
echo     INSTRUCCIONES: Compilar en Eclipse
echo ========================================================================
echo.
echo Por favor, sigue estos pasos en Eclipse:
echo.
echo   1. Abre Eclipse con el proyecto "todo.crud.list"
echo   2. Click DERECHO en el proyecto
echo   3. Selecciona: Run As -^> Maven build...
echo   4. En Goals, escribe: clean package
echo   5. (Opcional) Marca: Skip Tests
echo   6. Click en "Run"
echo   7. Espera a ver "BUILD SUCCESS"
echo.
echo ========================================================================
echo.
echo Despues de compilar, ejecuta este script de nuevo o:
echo   python create_installer.py
echo.
echo Para mas ayuda, lee: COMPILAR_EN_ECLIPSE.md
echo.

pause
exit /b 1

:run_script
echo ========================================================================
echo     Ejecutando script de instalador
echo ========================================================================
echo.

python create_installer.py

if errorlevel 1 (
    echo.
    echo ERROR: El script fallo
    pause
    exit /b 1
)

echo.
echo ========================================================================
echo     INSTALADOR CREADO EXITOSAMENTE
echo ========================================================================
echo.
echo Busca el instalador en: installer\output\
echo.

pause
