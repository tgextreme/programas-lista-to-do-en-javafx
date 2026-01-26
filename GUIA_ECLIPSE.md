# 🎯 Guía Específica para Usuarios de Eclipse

## ✨ Ventajas de Usar Eclipse

Si usas **Eclipse IDE**, el proceso es más simple porque:
- ✅ Eclipse incluye Maven (m2e) integrado
- ✅ No necesitas instalar Maven por separado
- ✅ El script detecta automáticamente el JAR compilado
- ✅ Puedes compilar con un click

---

## 🚀 Método Recomendado con Eclipse

### Paso 1: Compilar en Eclipse

1. **Abre Eclipse** con tu proyecto TODO CRUD List cargado

2. **Click derecho** en el proyecto en el explorador

3. **Selecciona:** `Run As` → `Maven build...`

4. **En "Goals"** escribe:
   ```
   clean package
   ```

5. **Click en "Run"**

6. **Espera** a que termine (verás "BUILD SUCCESS" en la consola)

### Paso 2: Ejecutar el Script Python

1. **Abre terminal** (CMD o PowerShell) en la carpeta del proyecto:
   ```bash
   cd "C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list"
   ```

2. **Ejecuta:**
   ```bash
   python create_installer.py
   ```

3. **¡Listo!** El script detectará el JAR compilado y continuará automáticamente

---

## 🔧 Alternativa: Script Batch

Si prefieres no usar Python, usa el script `.bat`:

### Paso 1: Compilar en Eclipse (igual que arriba)

### Paso 2: Ejecutar el .bat

1. **Doble click** en `create_installer.bat`

O desde terminal:
```batch
create_installer.bat
```

---

## 💡 El Script Detecta Automáticamente

El script Python actualizado incluye **3 métodos de detección**:

### Método 1: Maven del Sistema
```
Intenta ejecutar: mvn clean package
```

### Método 2: Maven Wrapper
```
Busca y ejecuta: mvnw.cmd
```

### Método 3: JAR Pre-compilado ⭐
```
Si ya compilaste en Eclipse, detecta:
target/todo.crud.list-0.0.1-SNAPSHOT.jar
```

**Si el JAR existe, salta la compilación y continúa directamente.**

---

## 🎨 Flujo Completo con Eclipse

```
┌─────────────────────────────────────────┐
│  Eclipse: Compilar Proyecto            │
│  (Run As → Maven build)                 │
└────────────┬────────────────────────────┘
             ↓
     [target/...jar creado]
             ↓
┌─────────────────────────────────────────┐
│  python create_installer.py             │
└────────────┬────────────────────────────┘
             ↓
     [Script detecta JAR]
             ↓
     [Salta compilación]
             ↓
     [Copia dependencias]
             ↓
     [Crea instalador]
             ↓
┌─────────────────────────────────────────┐
│  installer/output/                      │
│  TODO_CRUD_List_Setup_v1.0.0.exe        │
└─────────────────────────────────────────┘
```

---

## 🔍 Verificar que el JAR Existe

Antes de ejecutar el script, verifica:

```
Carpeta del proyecto/
└── target/
    └── todo.crud.list-0.0.1-SNAPSHOT.jar  ← Este archivo
```

Si este archivo existe, **el script funcionará sin problemas**, aunque Maven no esté en el PATH.

---

## 🚨 Solución de Problemas con Eclipse

### Problema: "Maven no encontrado"

**Solución:**
1. Compila en Eclipse primero (Run As → Maven build)
2. Verifica que `target/todo.crud.list-0.0.1-SNAPSHOT.jar` existe
3. Ejecuta el script de nuevo

El script usará el JAR existente.

### Problema: "Error al compilar"

**Solución A - Desde Eclipse:**
```
1. Project → Clean
2. Project → Build Project
3. Run As → Maven build (Goals: clean package)
```

**Solución B - Verificar consola de Eclipse:**
- Mira la pestaña "Console" en Eclipse
- Lee los errores de compilación
- Corrige errores de código si los hay
- Intenta compilar de nuevo

### Problema: "Dependencias no copiadas"

**Solución:**

El script buscará dependencias en:
1. Ejecutando `mvn dependency:copy-dependencies`
2. Repositorio local: `%USERPROFILE%\.m2\repository`
3. Carpeta `target/lib` si existe

Si falla, las dependencias principales están hardcodeadas:
- JavaFX Controls 21.0.1
- JavaFX FXML 21.0.1
- Gson 2.10.1

### Problema: "Python no encontrado"

**Alternativas:**
1. Usa `create_installer.bat` (no requiere Python)
2. Instala Python: https://www.python.org/downloads/
   - ⚠️ Marca "Add Python to PATH" al instalar

---

## 🎯 Comandos Maven Útiles en Eclipse

### Compilar (sin tests):
```
Goals: clean package -DskipTests
```

### Compilar (con tests):
```
Goals: clean package
```

### Solo compilar (sin limpiar):
```
Goals: package
```

### Ver dependencias:
```
Goals: dependency:tree
```

### Copiar dependencias manualmente:
```
Goals: dependency:copy-dependencies -DoutputDirectory=libs
```

---

## 📊 Configuración Recomendada en Eclipse

### Run Configuration Óptima:

1. **Click derecho en proyecto** → `Run As` → `Maven build...`

2. **Pestaña Main:**
   - Name: `Package TODO CRUD List`
   - Base directory: `${project_loc}`
   - Goals: `clean package -DskipTests`

3. **Pestaña JRE:**
   - Alternate JRE: Java JDK 17+

4. **Click "Run"**

5. **Guarda la configuración** para uso futuro

Ahora puedes ejecutar rápidamente desde:
`Run As` → `Package TODO CRUD List`

---

## 🎉 Ventajas del Método Eclipse

✅ **Más Simple:**
- No necesitas Maven en PATH
- Compilas con 2 clicks
- Interfaz visual

✅ **Más Rápido:**
- Eclipse cachea dependencias
- Compilación incremental
- No descarga cada vez

✅ **Más Visual:**
- Ves errores en tiempo real
- Autocompletado y ayuda
- Debug integrado

✅ **Más Profesional:**
- Control total desde el IDE
- Gestión de dependencias visual
- Integración con Git

---

## 🔄 Workflow Completo Recomendado

### Durante Desarrollo:

```
1. Editar código en Eclipse
2. Guardar (Auto-build de Eclipse)
3. Probar desde Eclipse (F11 o Run)
4. Commit a Git si funciona
```

### Para Crear Instalador:

```
1. En Eclipse: Run As → Maven build (clean package)
2. Verificar: BUILD SUCCESS
3. En terminal: python create_installer.py
4. Resultado: installer/output/TODO_CRUD_List_Setup.exe
```

### Para Distribuir:

```
1. Prueba el instalador en máquina limpia
2. Sube a GitHub Releases
3. Comparte el enlace
```

---

## 💡 Tips Pro para Eclipse

### 1. Atajos de Teclado Útiles

- `Ctrl+B` - Compilar proyecto
- `Alt+Shift+X, M` - Ejecutar Maven build anterior
- `F11` - Ejecutar aplicación en debug
- `Ctrl+F11` - Ejecutar aplicación normal

### 2. Maven en Eclipse (m2e)

Eclipse usa m2e (Maven to Eclipse) que:
- Sincroniza automáticamente el pom.xml
- Descarga dependencias automáticamente
- Actualiza el classpath
- Integra con el build de Eclipse

### 3. Ver Repositorio Local

```
Window → Preferences → Maven → User Settings
```

Verás la ubicación de `.m2/repository` donde están todas las dependencias.

### 4. Forzar Actualización de Dependencias

```
Click derecho en proyecto → Maven → Update Project
☑ Force Update of Snapshots/Releases
OK
```

---

## 📚 Recursos Adicionales

### Documentación Eclipse Maven:
https://www.eclipse.org/m2e/

### Guías Maven:
https://maven.apache.org/guides/

### JavaFX con Eclipse:
https://openjfx.io/openjfx-docs/#IDE-Eclipse

---

## ✅ Checklist Pre-Instalador

Antes de ejecutar `create_installer.py`:

- [ ] Proyecto sin errores en Eclipse
- [ ] Compilado con Maven (BUILD SUCCESS)
- [ ] Archivo `target/todo.crud.list-0.0.1-SNAPSHOT.jar` existe
- [ ] Python instalado (o usarás el .bat)
- [ ] Terminal abierta en carpeta del proyecto

**¡Listo para crear el instalador!** 🚀

---

**Para dudas:** Lee `INSTALLER_README.md` para más detalles.

**Ejecuta:**
```bash
python create_installer.py
```

O:
```batch
create_installer.bat
```
