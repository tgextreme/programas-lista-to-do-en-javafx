# 🚨 ERROR RESUELTO: "No goals have been specified"

## ❌ El Error Que Tuviste

```
[ERROR] No goals have been specified for this build.
```

Este es un error **muy común** cuando usas Maven en Eclipse.

---

## 🎯 CAUSA DEL ERROR

Ejecutaste: **Run As → Maven build** (sin puntos suspensivos)

Esto ejecuta la **última configuración guardada**, que probablemente está vacía o sin "Goals" especificados.

---

## ✅ SOLUCIÓN (Paso a Paso Visual)

### **Paso 1: Click Derecho en el Proyecto**

```
Package Explorer
├── 📁 todo.crud.list   ← CLICK DERECHO AQUÍ
│   ├── 📁 src
│   ├── 📁 target
│   └── 📄 pom.xml
```

### **Paso 2: Busca "Run As" en el Menú**

```
Menú Contextual:
├── New
├── Go Into
├── ...
├── Run As  ← POSICIONA EL MOUSE AQUÍ
│   ├── Java Application
│   ├── Maven build              ← ❌ NO ESTE
│   ├── Maven build...           ← ✅ ESTE (con ...)
│   ├── Maven clean
│   └── ...
└── ...
```

**IMPORTANTE:** Elige **"Maven build..."** con los **tres puntos** (...) al final.

### **Paso 3: Ventana de Configuración**

Aparecerá esta ventana:

```
┌─────────────────────────────────────────────┐
│ Edit Configuration and launch              │
├─────────────────────────────────────────────┤
│ Name: [todo.crud.list]                      │
│                                             │
│ Base directory:                             │
│ [${project_loc}]                     [Browse]│
│                                             │
│ Goals: [                    ]  ← AQUÍ!      │
│        ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼                     │
│        clean package           ← ESCRIBE    │
│                                             │
│ Profiles: [                         ]       │
│                                             │
│ User settings: [                    ]       │
│                                             │
│ ☐ Offline                                   │
│ ☐ Update Snapshots                          │
│ ☐ Debug Output                              │
│ ☑ Skip Tests  ← MARCA ESTO (opcional)      │
│ ☐ Non-recursive                             │
│                                             │
│ Parameter Name: [    ] Parameter Value: [  ]│
│                                             │
│          [Apply]  [Run]  [Close]            │
└─────────────────────────────────────────────┘
```

### **Paso 4: Escribe "clean package"**

En el campo **"Goals:"** escribe:
```
clean package
```

**O con tests deshabilitados (más rápido):**
```
clean package -DskipTests
```

### **Paso 5: Click en "Run"**

```
[Run]  ← CLICK AQUÍ
```

---

## 🎬 Secuencia Correcta Completa

```
1. Click DERECHO en "todo.crud.list"
   ↓
2. Run As → Maven build... (con ...)
   ↓
3. Goals: clean package
   ↓
4. (Opcional) ☑ Skip Tests
   ↓
5. [Run]
   ↓
6. Espera "BUILD SUCCESS"
   ↓
7. ✅ JAR creado en target/
```

---

## 📊 Diferencia Entre las Opciones

### ❌ "Maven build" (sin puntos)
```
Run As → Maven build

- Ejecuta la última configuración
- Si está vacía → ERROR
- No te permite editar Goals
```

### ✅ "Maven build..." (con puntos)
```
Run As → Maven build...

- Abre ventana de configuración
- Te permite escribir Goals
- Puedes editar opciones
- Guardas para uso futuro
```

---

## 🎯 "Goals" Más Comunes

### Para compilar y empaquetar:
```
clean package
```

### Para compilar sin tests:
```
clean package -DskipTests
```

### Para solo limpiar:
```
clean
```

### Para compilar sin limpiar:
```
package
```

### Para instalar en repositorio local:
```
clean install
```

---

## 💡 Después de la Primera Vez

Una vez que configures los "Goals" y ejecutes:

1. **Eclipse guarda la configuración**
2. **Aparece en el historial:** Run → Run History
3. **Puedes ejecutarla rápidamente:**
   - Click en el botón ▶️ Run
   - Selecciona tu configuración guardada
   - O usa: Alt + Shift + X, luego M

---

## 🔍 Verificar que Funcionó

Después de ejecutar, deberías ver en la **consola**:

```
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< todo.crud.list:todo.crud.list >--------------------
[INFO] Building todo.crud.list 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) ---
[INFO] Deleting target
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:resources ---
[INFO] Copying 2 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.10.1:compile ---
[INFO] Compiling 15 source files
[INFO] 
[INFO] --- maven-jar-plugin:3.2.2:jar ---
[INFO] Building jar: ...\target\todo.crud.list-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS  ← ¡BUSCA ESTO!
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.456 s
[INFO] Finished at: 2026-01-26T...
```

Y en Package Explorer:

```
├── 📁 todo.crud.list
│   ├── 📁 target
│   │   └── 📄 todo.crud.list-0.0.1-SNAPSHOT.jar  ← ¡AQUÍ!
```

---

## 🚀 Alternativa: Desde Terminal

Si prefieres no usar Eclipse:

```bash
# Ve a la carpeta del proyecto
cd "C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list"

# Ejecuta Maven
mvn clean package -DskipTests

# O si Maven no está en PATH, usa el de Eclipse:
# (Busca donde está instalado Eclipse)
```

---

## ✅ Ahora Sí: Crear el Instalador

Una vez que veas **"BUILD SUCCESS"** y el JAR exista:

```bash
# Ejecuta el script Python
python create_installer.py
```

El script:
1. ✅ Detectará el JAR compilado
2. ✅ Copiará dependencias
3. ✅ Convertirá logo.png a icon.ico (si Pillow está instalado)
4. ✅ Creará el instalador

---

## 📋 Checklist Final

Antes de ejecutar `create_installer.py`:

- [ ] Compilado en Eclipse con Goals: "clean package"
- [ ] Consola muestra "BUILD SUCCESS"
- [ ] Existe: `target/todo.crud.list-0.0.1-SNAPSHOT.jar`
- [ ] JAR tiene varios MB de tamaño
- [ ] (Opcional) Pillow instalado: `pip install Pillow`

**Todo OK? Ejecuta:**
```bash
python create_installer.py
```

---

## 🎉 Resumen

**Error:** No especificaste los "Goals" en Maven

**Solución:** Run As → Maven build... → Goals: clean package

**Resultado:** JAR compilado en target/

**Siguiente paso:** `python create_installer.py`

---

**¡Ahora sí puedes compilar correctamente!** 🚀

**Pasos:**
1. Eclipse: Run As → Maven build... → Goals: clean package → Run
2. Terminal: `python create_installer.py`
3. ✅ Instalador en: `installer/output/`
