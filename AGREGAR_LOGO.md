# 🎨 AGREGAR LOGO AL INSTALADOR

## ✅ Ya Tienes logo.png

Tu proyecto tiene: `logo.png`

Para usarlo en el instalador, necesitas convertirlo a formato `.ico`

---

## 🚀 MÉTODO 1: Automático con Pillow (Recomendado)

### Instalar Pillow:
```bash
pip install Pillow
```

### Ejecutar el script:
```bash
python create_installer.py
```

El script **automáticamente**:
- ✅ Detecta logo.png
- ✅ Lo convierte a icon.ico
- ✅ Lo usa en el instalador

---

## 🔧 MÉTODO 2: Conversión Manual Online

Si no quieres instalar Pillow:

### 1. Abre el conversor:
https://convertio.co/png-ico/

### 2. Sube logo.png

### 3. Configura tamaños:
```
☑ 16x16
☑ 32x32
☑ 48x48
☑ 64x64
☑ 128x128
☑ 256x256
```

### 4. Descarga como: `icon.ico`

### 5. Guarda en:
```
C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\icon.ico
```

### 6. Ejecuta el script:
```bash
python create_installer.py
```

---

## 🔧 MÉTODO 3: Conversión con GIMP (Gratis)

### 1. Descarga GIMP:
https://www.gimp.org/downloads/

### 2. Abre logo.png en GIMP

### 3. Escala a 256x256:
```
Image → Scale Image → 256x256
```

### 4. Exporta como ICO:
```
File → Export As...
Nombre: icon.ico
Formato: Microsoft Windows icon (*.ico)
```

### 5. Guarda en la raíz del proyecto

---

## 🔧 MÉTODO 4: PowerShell (Windows 11)

Si tienes .NET instalado:

```powershell
# Crear script de conversión
$code = @"
Add-Type -AssemblyName System.Drawing
`$img = [System.Drawing.Image]::FromFile("logo.png")
`$icon = [System.Drawing.Icon]::FromHandle(([System.Drawing.Bitmap]`$img).GetHicon())
`$stream = [System.IO.File]::OpenWrite("icon.ico")
`$icon.Save(`$stream)
`$stream.Close()
"@

# Ejecutar
powershell -Command $code
```

---

## 📊 Verificación

Después de convertir, verifica:

```
C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\
├── logo.png        ← Original
├── icon.ico        ← Convertido ✅
└── create_installer.py
```

---

## 🎯 Qué Hace el Script Actualizado

El script ahora:

1. **Busca logo.png** en la raíz del proyecto
2. **Intenta convertir automáticamente** a icon.ico (si Pillow está instalado)
3. **Usa icon.ico** en el instalador si existe
4. **Te avisa** si necesitas instalar Pillow o convertir manualmente

---

## 💡 Resultado Final

Tu instalador tendrá:

### ✅ Icono en el Instalador:
```
📦 TODO_CRUD_List_Setup_v1.0.0.exe
   ↑ Con tu logo
```

### ✅ Icono en Accesos Directos:
- Escritorio
- Menú Inicio
- Barra de tareas

### ✅ Icono en Ventana de Instalación:
- Wizard de instalación
- Barra de título
- Ventana de progreso

---

## 🚨 Si No Conviertes el Logo

Si ejecutas el script sin icon.ico:

- ⚠️ El instalador usará icono por defecto de Inno Setup
- ⚠️ La aplicación seguirá funcionando perfectamente
- ℹ️ Solo el icono será genérico

No es crítico, pero el icono personalizado se ve más profesional.

---

## ✅ Recomendación

**Opción más fácil:**

```bash
# 1. Instala Pillow una sola vez
pip install Pillow

# 2. Ejecuta el script
python create_installer.py

# ¡Listo! El script convierte automáticamente
```

---

## 📝 Características del Icono

Para mejores resultados, el logo.png debería ser:

- **Tamaño:** 256x256 px o mayor
- **Formato:** PNG con fondo transparente
- **Forma:** Cuadrada (no rectangular)
- **Estilo:** Simple y reconocible en tamaños pequeños

Si tu logo.png cumple esto, ¡perfecto! El script lo manejará.

---

## 🎨 Personalización Adicional

### Para cambiar imágenes del wizard:

El instalador usa imágenes por defecto de Inno Setup, pero puedes personalizarlas:

1. **Imagen grande del wizard** (164x314 px):
   - Crea: `wizard_image.bmp`
   - En el script: `WizardImageFile=wizard_image.bmp`

2. **Imagen pequeña del wizard** (55x58 px):
   - Crea: `wizard_small.bmp`
   - En el script: `WizardSmallImageFile=wizard_small.bmp`

---

## 🎉 Ejecutar Ahora

Una vez que tengas icon.ico (automático o manual):

```bash
# Compila en Eclipse primero
# Luego:
python create_installer.py
```

El instalador incluirá tu logo automáticamente.

---

**¡Tu instalador será más profesional con tu logo personalizado!** 🎨✨
