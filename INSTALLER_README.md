# 🚀 Creador de Instalador Windows - TODO CRUD List

Este script automatiza la creación de un instalador profesional tipo "Next, Next, Install" para Windows 11.

## 📋 Requisitos

### Obligatorios:
- ✅ **Java JDK 17+** ([Descargar](https://adoptium.net/))
- ✅ **Eclipse IDE** (con Maven integrado m2e) O **Maven standalone**
- ✅ **Python 3.7+** ([Descargar](https://www.python.org/downloads/))

### Opcionales:
- 🔧 **Inno Setup 6** ([Descargar](https://jrsoftware.org/isdl.php)) - Para compilar el instalador automáticamente
- 🔧 **jpackage** (Incluido en JDK 14+) - Para crear ejecutable nativo

### 💡 Nota sobre Maven:
Este script está **optimizado para Eclipse**. Si tienes Eclipse instalado:
- No necesitas Maven standalone en el PATH
- El script usará el Maven embebido de Eclipse (m2e)
- O compilarás el proyecto desde Eclipse y el script lo detectará

---

## 🎯 Uso Rápido

### Opción 1: Script Python (Recomendado)

```bash
# Ejecutar el script
python create_installer.py
```

El script hará automáticamente:
1. ✅ Verificar requisitos (Java, Maven, jpackage)
2. ✅ Limpiar directorios anteriores
3. ✅ Compilar proyecto con Maven
4. ✅ Copiar dependencias
5. ✅ Crear ejecutable con jpackage (si está disponible)
6. ✅ Generar script de Inno Setup
7. ✅ Compilar instalador (si Inno Setup está instalado)

### Opción 2: Script Batch (Windows)

```batch
create_installer.bat
```

---

## 📦 Resultado

Después de ejecutar el script, encontrarás:

```
installer/
├── build/                          # Archivos temporales de construcción
│   ├── todo.crud.list-0.0.1-SNAPSHOT.jar
│   ├── libs/                       # Dependencias
│   └── TODOCRUDList/              # Aplicación empaquetada (si usó jpackage)
├── output/                         # 🎉 INSTALADOR FINAL AQUÍ
│   └── TODO_CRUD_List_Setup_v1.0.0.exe  ← ¡Tu instalador!
└── setup_script.iss                # Script de Inno Setup
```

---

## 🎨 Características del Instalador

✨ **Instalador Profesional:**
- 📱 Interfaz moderna estilo Windows 11
- 🌍 Soporte multiidioma (Español/Inglés)
- 🔍 Detección automática de Java
- 🖥️ Creación de accesos directos (Escritorio, Menú Inicio)
- 🗑️ Desinstalador completo
- 📝 Instalación silenciosa disponible: `/SILENT` o `/VERYSILENT`

✨ **Opciones de Instalación:**
- Elegir directorio de instalación
- Crear icono en escritorio (opcional)
- Crear acceso rápido (opcional)
- Ejecutar aplicación al finalizar

---

## 🛠️ Instalación de Requisitos

### 1. Instalar Java JDK 17+

```bash
# Verifica si ya lo tienes
java -version

# Si no, descarga desde:
# https://adoptium.net/
```

### 2. Instalar/Usar Maven

**Opción A: Usar Eclipse (Recomendado)**
```bash
# Eclipse incluye Maven (m2e) integrado
# No necesitas instalar nada adicional

# Para compilar desde Eclipse:
# 1. Click derecho en el proyecto
# 2. Run As → Maven build...
# 3. Goals: clean package
# 4. Run
```

**Opción B: Maven standalone**
```bash
# Verifica si ya lo tienes
mvn -version

# Si no, descarga desde:
# https://maven.apache.org/download.cgi
# Y agrega Maven al PATH
```

### 💡 Instrucción Especial para Eclipse:
Si usas Eclipse y Maven no está en el PATH del sistema:

1. **Antes de ejecutar el script Python:**
   ```
   En Eclipse:
   - Click derecho en el proyecto
   - Run As → Maven build...
   - Goals: clean package
   - Click Run
   ```

2. **Luego ejecuta el script:**
   ```bash
   python create_installer.py
   ```

El script detectará que el JAR ya está compilado y continuará automáticamente.

### 3. Instalar Python (si no lo tienes)

```bash
# Verifica si ya lo tienes
python --version

# Si no, descarga desde:
# https://www.python.org/downloads/
# ⚠️ Marca "Add Python to PATH" durante instalación
```

### 4. Instalar Inno Setup (Opcional pero recomendado)

Descarga e instala desde: https://jrsoftware.org/isdl.php

**Si no instalas Inno Setup:**
- El script generará el archivo `.iss`
- Podrás compilarlo manualmente después

---

## 🔧 Solución de Problemas

### ❌ "Java no encontrado"
```bash
# Verifica instalación
java -version

# Agrega Java al PATH:
# Panel de Control → Sistema → Configuración avanzada → Variables de entorno
# Agrega: C:\Program Files\Java\jdk-17\bin
```

### ❌ "Maven no encontrado"
```bash
# Verifica instalación
mvn -version

# Agrega Maven al PATH:
# Agrega: C:\apache-maven-3.9.x\bin
```

### ❌ "jpackage no encontrado"
```bash
# jpackage está incluido en JDK 14+
# Si tienes JDK 17, debería estar disponible
java --version  # Verifica que sea JDK, no JRE
```

### ❌ "Inno Setup no encontrado"
Dos opciones:
1. **Instala Inno Setup:** https://jrsoftware.org/isdl.php
2. **Compila manualmente:**
   - Abre `installer/setup_script.iss` con Inno Setup
   - Presiona F9 o click en "Compile"

### ❌ Error al compilar proyecto
```bash
# Limpia el proyecto manualmente
mvn clean

# Intenta compilar solo el proyecto
mvn package -DskipTests

# Si hay errores, revisa:
# - Que no haya errores de código
# - Que todas las dependencias estén en pom.xml
```

---

## 📝 Personalización

### Cambiar información de la aplicación

Edita el archivo `create_installer.py`:

```python
# Líneas 13-18
APP_NAME = "TODO CRUD List"          # Nombre de la aplicación
APP_VERSION = "1.0.0"                # Versión
APP_VENDOR = "InfoGonzalez"          # Vendedor/Autor
APP_DESCRIPTION = "Gestor de Tareas" # Descripción
```

### Agregar icono personalizado

1. Crea un archivo `icon.ico` en la raíz del proyecto
2. El script lo detectará automáticamente
3. Puedes crear iconos en: https://favicon.io/

### Modificar script de instalación

El script de Inno Setup generado está en:
```
installer/setup_script.iss
```

Puedes editarlo para:
- Cambiar colores/imágenes
- Agregar más idiomas
- Personalizar mensajes
- Agregar pasos adicionales

---

## 🎯 Uso del Instalador Creado

### Instalación Normal (Con interfaz)
```bash
TODO_CRUD_List_Setup_v1.0.0.exe
```

### Instalación Silenciosa (Sin interfaz)
```bash
TODO_CRUD_List_Setup_v1.0.0.exe /SILENT
```

### Instalación Muy Silenciosa (Sin nada)
```bash
TODO_CRUD_List_Setup_v1.0.0.exe /VERYSILENT
```

### Desinstalación
```
Panel de Control → Programas → Desinstalar TODO CRUD List
```

O desde línea de comandos:
```bash
"%ProgramFiles%\TODO CRUD List\unins000.exe"
```

---

## 📊 Estructura del Instalador

```
Usuario instala → TODO_CRUD_List_Setup_v1.0.0.exe
                  ↓
                  [Bienvenida]
                  ↓
                  [Licencia]
                  ↓
                  [Seleccionar directorio]
                  (Por defecto: C:\Program Files\TODO CRUD List)
                  ↓
                  [Seleccionar opciones]
                  ☑ Crear icono en escritorio
                  ☑ Crear acceso rápido
                  ↓
                  [Instalando...]
                  - Copia archivos
                  - Crea accesos directos
                  - Registra en sistema
                  ↓
                  [Finalizado]
                  ☑ Ejecutar TODO CRUD List
                  ↓
                  ¡Aplicación instalada! 🎉
```

---

## 🔍 Verificación Post-Instalación

Después de instalar, el usuario tendrá:

✅ **Archivos instalados:**
```
C:\Program Files\TODO CRUD List\
├── TODOCRUDList.exe              # Ejecutable principal
├── app/                          # Aplicación JavaFX
├── runtime/                      # Java runtime (si se incluye)
└── unins000.exe                  # Desinstalador
```

✅ **Accesos directos:**
- 🖥️ Escritorio: `TODO CRUD List.lnk`
- 📁 Menú Inicio: `TODO CRUD List`
- 🗑️ Menú Inicio: `Desinstalar TODO CRUD List`

✅ **Registro Windows:**
- Aparece en "Programas y características"
- Tiene desinstalador registrado
- Información de versión correcta

---

## 🚀 Distribución

### Compartir el instalador:

1. **GitHub Releases:**
   ```bash
   # Sube el .exe a GitHub Releases
   gh release create v1.0.0 installer/output/TODO_CRUD_List_Setup_v1.0.0.exe
   ```

2. **Google Drive / OneDrive:**
   - Sube el archivo .exe
   - Comparte el enlace

3. **Tu propio servidor:**
   - Sube el .exe
   - Comparte la URL de descarga

### Información para usuarios:

```markdown
## 📥 Instalación

1. Descarga: [TODO_CRUD_List_Setup_v1.0.0.exe](URL)
2. Ejecuta el instalador
3. Sigue los pasos (Next, Next, Install)
4. ¡Listo!

**Requisitos:** Windows 11/10, Java 17+ debe estar instalado
```

---

## 📚 Referencias

- [Inno Setup Documentation](https://jrsoftware.org/ishelp/)
- [jpackage Guide](https://docs.oracle.com/en/java/javase/17/docs/specs/man/jpackage.html)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 💡 Tips Pro

### Reducir tamaño del instalador:
1. Usa compresión máxima en Inno Setup (ya configurado)
2. Excluye archivos de test
3. Usa jlink para crear runtime mínimo de Java

### Firma digital del instalador:
```bash
# Con signtool (Windows SDK)
signtool sign /f certificate.pfx /p password /t http://timestamp.server TODO_CRUD_List_Setup.exe
```

### Auto-actualización:
- Implementa verificador de actualizaciones en la app
- Usa GitHub API para verificar releases
- Descarga e instala automáticamente

---

## ✅ Checklist Final

Antes de distribuir, verifica:

- [ ] El instalador se ejecuta sin errores
- [ ] La aplicación se instala correctamente
- [ ] Los accesos directos funcionan
- [ ] La aplicación inicia correctamente
- [ ] El desinstalador funciona
- [ ] No quedan archivos después de desinstalar
- [ ] La versión es correcta
- [ ] El icono se muestra correctamente
- [ ] La licencia es la correcta

---

**¡Listo para crear tu instalador profesional!** 🎉

Ejecuta: `python create_installer.py`
