# ⚡ COMPILAR PROYECTO EN ECLIPSE - GUÍA VISUAL PASO A PASO

## 🎯 Objetivo
Compilar el proyecto TODO CRUD List en Eclipse para generar el archivo JAR necesario para el instalador.

---

## 📋 Pasos Detallados

### **Paso 1: Abrir Eclipse**
```
✓ Abre Eclipse IDE
✓ Asegúrate de que el workspace esté cargado:
  C:\Users\usuario\Workspace Eclipse YouTube\
```

### **Paso 2: Verificar que el Proyecto Esté Visible**
```
En el panel izquierdo (Package Explorer o Project Explorer):
✓ Deberías ver: todo.crud.list
```

Si no lo ves:
```
File → Open Projects from File System...
→ Directory: C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list
→ Finish
```

---

### **Paso 3: Click Derecho en el Proyecto** 👆

```
┌─────────────────────────────────┐
│ Package Explorer                │
├─────────────────────────────────┤
│  📁 todo.crud.list   ← AQUÍ    │
│    📁 src                       │
│    📁 target                    │
│    📄 pom.xml                   │
│    ...                          │
└─────────────────────────────────┘

1. Localiza "todo.crud.list" en el explorador
2. Click DERECHO sobre el nombre del proyecto
3. Se abrirá un menú contextual
```

---

### **Paso 4: Seleccionar "Run As"**

```
Menú Contextual:
├── New
├── Go Into
├── Open in New Window
├── ...
├── Run As  ← AQUÍ (posiciona el mouse)
│   ├── Java Application
│   ├── Maven build   ← CLICK AQUÍ
│   ├── Maven build...  ← O CLICK AQUÍ (recomendado)
│   └── ...
└── ...
```

**Importante:** Hay 2 opciones similares:
- `Maven build` - Ejecuta la última configuración
- `Maven build...` - Abre ventana de configuración ⭐ (ELIGE ESTA)

---

### **Paso 5: Configurar Maven Build**

Aparecerá una ventana: **"Edit Configuration"**

```
┌─────────────────────────────────────────────┐
│ Edit Configuration                          │
├─────────────────────────────────────────────┤
│ Name: [todo.crud.list]                      │
│                                             │
│ Base directory:                             │
│ [${project_loc}]                     [Browse]│
│                                             │
│ Goals: [                    ]  ← ESCRIBE AQUÍ│
│        clean package                        │
│                                             │
│ Profiles: [                         ]       │
│                                             │
│ ☑ Resolve Workspace artifacts              │
│ ☑ Update Snapshots                          │
│ ☐ Skip Tests  ← MARCA ESTO (opcional)      │
│                                             │
│          [Apply]  [Run]  [Close]            │
└─────────────────────────────────────────────┘
```

**Configuración:**
1. **Goals:** Escribe `clean package`
2. **Skip Tests:** Marca esta opción (opcional, para compilar más rápido)
3. Click en **[Run]**

---

### **Paso 6: Observar la Compilación**

La pestaña **Console** mostrará el progreso:

```
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< todo.crud.list:todo.crud.list >--------------------
[INFO] Building todo.crud.list 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ todo.crud.list ---
[INFO] Deleting target
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) ---
[INFO] Copying 2 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.10.1:compile (default-compile) ---
[INFO] Compiling 15 source files to target\classes
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:testResources ---
[INFO] 
[INFO] --- maven-compiler-plugin:3.10.1:testCompile ---
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) ---
[INFO] Tests are skipped.
[INFO] 
[INFO] --- maven-jar-plugin:3.2.2:jar (default-jar) ---
[INFO] Building jar: ...\target\todo.crud.list-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS  ← ¡BUSCA ESTO!
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.456 s
[INFO] Finished at: 2026-01-26T...
[INFO] ------------------------------------------------------------------------
```

**¡ÉXITO!** Si ves `BUILD SUCCESS`, la compilación funcionó.

---

### **Paso 7: Verificar que el JAR Existe**

```
En Package Explorer:
├── 📁 todo.crud.list
│   ├── 📁 src
│   ├── 📁 target  ← Expande esta carpeta
│   │   ├── 📁 classes
│   │   ├── 📁 test-classes
│   │   └── 📄 todo.crud.list-0.0.1-SNAPSHOT.jar  ← ¡AQUÍ ESTÁ!
│   └── 📄 pom.xml
```

**Verificación manual:**
```
Abre el Explorador de Windows:
C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\target\

Deberías ver:
📄 todo.crud.list-0.0.1-SNAPSHOT.jar (varios MB)
```

---

### **Paso 8: Ejecutar el Script Python**

Ahora que el JAR existe, ejecuta el script de nuevo:

```powershell
cd "C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list"
python create_installer.py
```

El script detectará el JAR y continuará automáticamente.

---

## 🚨 Solución de Problemas

### ❌ Error: "Failed to execute goal..."

**Posibles causas:**
1. Errores en el código fuente
2. Dependencias no descargadas

**Solución:**
```
1. En Eclipse: Project → Clean → Clean all projects
2. Click derecho en proyecto → Maven → Update Project
3. Marca: ☑ Force Update of Snapshots/Releases
4. Click OK
5. Intenta compilar de nuevo
```

### ❌ Error: "Cannot find symbol" o errores de compilación

**Solución:**
```
1. Verifica que no haya errores en el código (marcas rojas en Eclipse)
2. Si hay errores, corrígelos primero
3. Project → Clean
4. Intenta compilar de nuevo
```

### ❌ Maven build no aparece en el menú

**Solución:**
```
1. Verifica que m2e esté instalado:
   Help → Eclipse Marketplace
   Busca: "m2e" o "Maven"
   Instala "Maven Integration for Eclipse (m2e)"

2. Reinicia Eclipse

3. Click derecho en proyecto → Configure → Convert to Maven Project
```

### ❌ "Build Failure" sin detalles

**Solución:**
```
1. Revisa la consola completa
2. Busca líneas con [ERROR]
3. Lee el mensaje de error específico
4. Si es problema de dependencias:
   - Click derecho → Maven → Update Project
   - ☑ Force Update
```

---

## ⚡ Atajos Rápidos

Una vez que tengas la configuración guardada:

### Método Rápido:
```
1. Click en el proyecto
2. Presiona: Alt + Shift + X, luego M
   (Ejecuta el último Maven build)
```

### Desde la Barra de Herramientas:
```
1. Click en el ícono "Run" (▶️)
2. Selecciona tu configuración Maven guardada
```

---

## 💾 Guardar Configuración para Uso Futuro

Después de configurar una vez:

```
1. Run → Run Configurations...
2. En el panel izquierdo: Maven Build
3. Verás tu configuración guardada
4. Puedes editarla o duplicarla
5. Dale un nombre descriptivo: "Package TODO CRUD List"
```

Ahora aparecerá en:
- Run → Run History
- Botón Run de la barra de herramientas

---

## ✅ Checklist Final

Antes de ejecutar el script Python, verifica:

- [ ] Eclipse abierto con el proyecto
- [ ] Maven build ejecutado
- [ ] Consola muestra "BUILD SUCCESS"
- [ ] Archivo existe: `target/todo.crud.list-0.0.1-SNAPSHOT.jar`
- [ ] Tamaño del JAR es > 1 MB

**Si todos los checks están OK:**

```powershell
python create_installer.py
```

---

## 🎉 ¡Listo!

Una vez compilado en Eclipse, el script Python:
1. ✅ Detectará el JAR automáticamente
2. ✅ Copiará dependencias
3. ✅ Creará el instalador
4. ✅ Generará: `installer/output/TODO_CRUD_List_Setup_v1.0.0.exe`

---

## 📞 Ayuda Adicional

**Si sigues teniendo problemas:**
1. Revisa los errores específicos en la consola de Eclipse
2. Asegúrate de que el proyecto no tenga errores de compilación
3. Verifica que todas las dependencias en `pom.xml` sean correctas
4. Intenta: Project → Clean → Build Project

**Luego ejecuta:**
```
Run As → Maven build... → Goals: clean package
```

---

**¡Ahora puedes compilar y crear tu instalador!** 🚀
