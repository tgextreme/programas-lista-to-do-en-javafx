@echo off
REM Script para crear acceso directo a TODO CRUD List en el Escritorio
REM Sin ventana CMD molesta

echo.
echo ========================================================================
echo     Crear Acceso Directo a TODO CRUD List
echo ========================================================================
echo.

set PROJECT_DIR=%~dp0
set DESKTOP=%USERPROFILE%\Desktop
set SHORTCUT_NAME=TODO CRUD List.lnk
set VBS_LAUNCHER=%PROJECT_DIR%launch.vbs
set ICON_FILE=%PROJECT_DIR%icon.ico

echo Creando acceso directo en el Escritorio...
echo.

REM Crear script VBS temporal para crear el acceso directo
set TEMP_VBS=%TEMP%\create_shortcut.vbs
echo Set oWS = WScript.CreateObject("WScript.Shell") > "%TEMP_VBS%"
echo sLinkFile = "%DESKTOP%\%SHORTCUT_NAME%" >> "%TEMP_VBS%"
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> "%TEMP_VBS%"
echo oLink.TargetPath = "%VBS_LAUNCHER%" >> "%TEMP_VBS%"
echo oLink.WorkingDirectory = "%PROJECT_DIR%" >> "%TEMP_VBS%"
echo oLink.Description = "TODO CRUD List - Gestor de Tareas" >> "%TEMP_VBS%"

REM Añadir icono si existe
if exist "%ICON_FILE%" (
    echo oLink.IconLocation = "%ICON_FILE%, 0" >> "%TEMP_VBS%"
    echo Icono encontrado: icon.ico
) else (
    echo Icono no encontrado - usando icono por defecto
)

echo oLink.Save >> "%TEMP_VBS%"

REM Ejecutar el script VBS
cscript //nologo "%TEMP_VBS%"

REM Limpiar
del "%TEMP_VBS%"

if exist "%DESKTOP%\%SHORTCUT_NAME%" (
    echo.
    echo ========================================================================
    echo     ACCESO DIRECTO CREADO EXITOSAMENTE
    echo ========================================================================
    echo.
    echo Ubicacion: %DESKTOP%\%SHORTCUT_NAME%
    echo.
    echo Ahora puedes ejecutar TODO CRUD List desde tu Escritorio
    echo sin que aparezca la ventana CMD molesta.
    echo.
) else (
    echo.
    echo ERROR: No se pudo crear el acceso directo
    echo.
)

pause
