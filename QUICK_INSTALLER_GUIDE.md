# 🎯 GUÍA RÁPIDA: Crear Instalador Windows

## 🚀 Opción 1: Instalador Completo (Recomendado)

### Requisitos:
1. ✅ Java JDK 17+ instalado
2. ✅ Maven instalado
3. ✅ Python 3.7+ instalado
4. 🔧 Inno Setup (opcional, para compilar automáticamente)

### Ejecutar:
```bash
python create_installer.py
```

### Resultado:
```
installer/output/TODO_CRUD_List_Setup_v1.0.0.exe
```

✨ **Instalador profesional tipo "Next, Next, Install"**

---

## 🔧 Opción 2: Simple (Sin Python)

### Requisitos:
1. ✅ Java JDK 17+ instalado
2. ✅ Maven instalado

### Ejecutar:
```batch
create_installer.bat
```

### Resultado:
```
installer/build/
├── todo.crud.list-0.0.1-SNAPSHOT.jar
├── launch.bat  ← Ejecutar esto
└── libs/       ← Dependencias
```

**Distribuye estos archivos en un ZIP**

---

## 📦 Opción 3: Instalador Manual con Inno Setup

### Pasos:

1. **Ejecuta el script Python o BAT:**
   ```bash
   python create_installer.py
   ```

2. **Si Inno Setup no compila automáticamente:**
   - Instala Inno Setup: https://jrsoftware.org/isdl.php
   - Abre `installer/setup_script.iss`
   - Presiona F9 o click en "Compile"

3. **Resultado:**
   ```
   installer/output/TODO_CRUD_List_Setup_v1.0.0.exe
   ```

---

## ⚡ Instalación de Requisitos

### Java JDK 17+
```bash
# Verificar:
java -version

# Instalar desde:
https://adoptium.net/
```

### Maven
```bash
# Verificar:
mvn -version

# Instalar desde:
https://maven.apache.org/download.cgi
# Agregar al PATH: C:\apache-maven-x.x.x\bin
```

### Python 3.7+
```bash
# Verificar:
python --version

# Instalar desde:
https://www.python.org/downloads/
# ⚠️ Marcar "Add Python to PATH"
```

### Inno Setup (Opcional)
```
https://jrsoftware.org/isdl.php
```

---

## 🎉 Resultado Final

### Instalador Profesional:
- ✅ Interfaz moderna Windows 11
- ✅ Multiidioma (Español/Inglés)
- ✅ Detección automática de Java
- ✅ Accesos directos (Escritorio + Menú)
- ✅ Desinstalador completo

### Tamaño estimado:
- **Instalador:** ~20-40 MB (comprimido)
- **Instalado:** ~50-80 MB

---

## 🐛 Solución Rápida de Problemas

### "Java no encontrado"
```bash
# Instala JDK 17+ y agrega al PATH:
set PATH=%PATH%;C:\Program Files\Java\jdk-17\bin
```

### "Maven no encontrado"
```bash
# Agrega Maven al PATH:
set PATH=%PATH%;C:\apache-maven-3.9.x\bin
```

### "Python no encontrado"
```bash
# Reinstala Python marcando "Add to PATH"
# O agrega manualmente:
set PATH=%PATH%;C:\Python311
```

### Error al compilar
```bash
# Limpia y vuelve a intentar:
mvn clean
python create_installer.py
```

---

## 📤 Distribuir el Instalador

### Subir a GitHub Releases:
```bash
gh release create v1.0.0 installer/output/TODO_CRUD_List_Setup_v1.0.0.exe
```

### Compartir:
- Google Drive / OneDrive
- Dropbox
- Tu propio servidor

---

## ✅ Checklist

Antes de distribuir, verifica:

- [ ] Instalador se ejecuta sin errores
- [ ] Aplicación inicia correctamente
- [ ] Accesos directos funcionan
- [ ] Desinstalador funciona
- [ ] No quedan archivos residuales

---

**¿Dudas?** Lee `INSTALLER_README.md` para documentación completa.

**¡Listo para crear!** Ejecuta: `python create_installer.py` 🚀
