# Solución: Eliminar ventana CMD molesta

## Problema
Cuando ejecutas la aplicación TODO CRUD List, aparece una ventana de CMD negra que no puedes cerrar sin cerrar también la aplicación.

## Solución Aplicada

### 1. Usar el archivo launch.vbs (RECOMENDADO)
- **Archivo:** `launch.vbs` (en la raíz del proyecto)
- **Descripción:** Ejecuta la aplicación sin mostrar ventana de CMD
- **Uso:** Doble click en `launch.vbs`

### 2. Usar el archivo launch.bat mejorado
- **Archivo:** `launch.bat` (en la raíz del proyecto)
- **Descripción:** Usa `javaw` en lugar de `java` para minimizar la ventana CMD
- **Uso:** Doble click en `launch.bat`

### 3. Para la versión instalada
- **Archivo:** `installer\output\TODOCRUDList\app\launch.vbs`
- **Descripción:** Ejecuta la aplicación instalada sin ventana CMD
- **Uso:** Doble click en este archivo

## Crear un Acceso Directo (Opcional)

1. Click derecho en `launch.vbs`
2. Selecciona "Enviar a" → "Escritorio (crear acceso directo)"
3. Renombra el acceso directo a "TODO CRUD List"
4. (Opcional) Click derecho → Propiedades → Cambiar icono → selecciona `icon.ico` del proyecto

## Cambios Realizados en el Código

### 1. Modificado `create_installer.py`
- Ahora genera archivos `.vbs` que no muestran ventana CMD
- Usa `javaw` en lugar de `java` en los archivos `.bat`
- Eliminó la opción `--win-console` de jpackage

### 2. Creados nuevos archivos de lanzamiento
- `launch.vbs` - Launcher principal sin CMD
- `launch.bat` - Launcher mejorado con javaw
- `installer\output\TODOCRUDList\app\launch.vbs` - Para versión instalada

## Diferencia entre java y javaw

- **java**: Ejecuta la aplicación CON ventana de consola
- **javaw**: Ejecuta la aplicación SIN ventana de consola (para aplicaciones GUI)

## Próximos Pasos

1. **Usar ahora:** Ejecuta `launch.vbs` para probar
2. **Reconstruir instalador:** Ejecuta `python create_installer.py` para regenerar el instalador sin ventana CMD
3. **Verificar TODOCRUDList.exe:** El ejecutable generado ahora NO mostrará ventana CMD

## Notas

- Si ves algún error, ahora no podrás ver la consola. Para depurar, usa `launch.bat` temporalmente.
- El archivo VBS es la mejor solución para aplicaciones GUI JavaFX.
- Los archivos generados funcionan en Windows 10/11.
