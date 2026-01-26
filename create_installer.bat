@echo off
REM ========================================================================
REM Script para crear instalador de TODO CRUD List para Windows
REM Compatible con Maven de Eclipse
REM ========================================================================

echo.
echo ========================================================================
echo     Creador de Instalador - TODO CRUD List v1.0.0
echo     (Compatible con Eclipse Maven)
echo ========================================================================
echo.

REM Verificar Java
echo [1/7] Verificando Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java no encontrado. Por favor instala JDK 17 o superior.
    echo Descarga desde: https://adoptium.net/
    pause
    exit /b 1
)
echo OK: Java encontrado

REM Verificar Maven (opcional con Eclipse)
echo [2/7] Verificando Maven...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo AVISO: Maven no encontrado en PATH del sistema
    echo INFO: Si usas Eclipse, el proyecto debe estar compilado
    echo INFO: En Eclipse: Click derecho en proyecto -^> Run As -^> Maven build
) else (
    echo OK: Maven encontrado
)

REM Verificar si el JAR existe (compilado desde Eclipse)
if exist "target\todo.crud.list-0.0.1-SNAPSHOT.jar" (
    echo OK: Proyecto ya compilado
    goto :skip_compile
)

REM Intentar compilar con Maven
echo [3/7] Compilando proyecto...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ================================================================
    echo ATENCION: Maven no disponible
    echo ================================================================
    echo.
    echo Por favor, compila el proyecto en Eclipse:
    echo   1. Click derecho en el proyecto
    echo   2. Run As -^> Maven build...
    echo   3. Goals: clean package
    echo   4. Run
    echo.
    echo Luego ejecuta este script de nuevo.
    echo ================================================================
    pause
    exit /b 1
)

call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Fallo al compilar el proyecto
    pause
    exit /b 1
)
echo OK: Proyecto compilado

:skip_compile

REM Limpiar directorios
echo [4/7] Limpiando directorios anteriores...
if exist "installer\build" rmdir /s /q "installer\build"
if exist "installer\output" rmdir /s /q "installer\output"
mkdir "installer\build" 2>nul
mkdir "installer\output" 2>nul
mkdir "installer\build\libs" 2>nul
echo OK: Directorios preparados

REM Copiar JAR
echo [5/7] Copiando archivos...
if not exist "target\todo.crud.list-0.0.1-SNAPSHOT.jar" (
    echo ERROR: JAR no encontrado en target\
    echo Por favor compila el proyecto en Eclipse primero
    pause
    exit /b 1
)
copy "target\todo.crud.list-0.0.1-SNAPSHOT.jar" "installer\build\" >nul
echo OK: JAR copiado

REM Copiar dependencias
echo [6/7] Copiando dependencias...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo AVISO: Maven no disponible, copiando desde repositorio local...
    
    REM Copiar desde .m2 repository
    set M2_REPO=%USERPROFILE%\.m2\repository
    
    if exist "%M2_REPO%\org\openjfx\javafx-controls\21.0.1" (
        copy "%M2_REPO%\org\openjfx\javafx-controls\21.0.1\*.jar" "installer\build\libs\" 2>nul
    )
    if exist "%M2_REPO%\org\openjfx\javafx-fxml\21.0.1" (
        copy "%M2_REPO%\org\openjfx\javafx-fxml\21.0.1\*.jar" "installer\build\libs\" 2>nul
    )
    if exist "%M2_REPO%\com\google\code\gson\gson\2.10.1" (
        copy "%M2_REPO%\com\google\code\gson\gson\2.10.1\*.jar" "installer\build\libs\" 2>nul
    )
    
    echo OK: Dependencias copiadas desde repositorio local
) else (
    call mvn dependency:copy-dependencies -DoutputDirectory=installer/build/libs
    echo OK: Dependencias copiadas con Maven
)

REM Crear script de lanzamiento
echo [7/7] Creando launcher...
echo @echo off > "installer\build\launch.bat"
echo java -jar "%%~dp0todo.crud.list-0.0.1-SNAPSHOT.jar" >> "installer\build\launch.bat"
echo OK: Launcher creado

echo.
echo ========================================================================
echo     ARCHIVOS PREPARADOS EXITOSAMENTE
echo ========================================================================
echo.
echo Los archivos estan listos en: installer\build\
echo.
echo OPCIONES:
echo.
echo 1. Ejecutar script Python para crear instalador completo:
echo    python create_installer.py
echo.
echo 2. Probar la aplicacion:
echo    cd installer\build
echo    launch.bat
echo.
echo 3. Distribuir manualmente:
echo    - Comprime la carpeta installer\build\
echo    - Distribuye el ZIP
echo.
echo Para crear instalador profesional:
echo - Instala Inno Setup: https://jrsoftware.org/isdl.php
echo - Ejecuta: python create_installer.py
echo.

pause
