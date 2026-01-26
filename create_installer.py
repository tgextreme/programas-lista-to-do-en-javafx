#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script para crear instalador de TODO CRUD List para Windows 11
Genera un instalador .exe estilo "Next, Next, Install"
"""

import os
import sys
import subprocess
import shutil
from pathlib import Path
import json

# ============================================================================
# CONFIGURACIÓN
# ============================================================================

APP_NAME = "TODO CRUD List"
APP_VERSION = "1.0.0"
APP_VENDOR = "InfoGonzalez"
APP_DESCRIPTION = "Gestor de Tareas con JavaFX"
MAIN_CLASS = "com.inforgonzalez.todo.crud.list.App"
MAIN_JAR = "todo.crud.list-0.0.1-SNAPSHOT.jar"

# Rutas
PROJECT_DIR = Path(__file__).parent.absolute()
TARGET_DIR = PROJECT_DIR / "target"
INSTALLER_DIR = PROJECT_DIR / "installer"
BUILD_DIR = INSTALLER_DIR / "build"
OUTPUT_DIR = INSTALLER_DIR / "output"

# ============================================================================
# COLORES PARA CONSOLA
# ============================================================================

class Colors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

def print_header(text):
    print(f"\n{Colors.HEADER}{Colors.BOLD}{'='*70}{Colors.ENDC}")
    print(f"{Colors.HEADER}{Colors.BOLD}{text.center(70)}{Colors.ENDC}")
    print(f"{Colors.HEADER}{Colors.BOLD}{'='*70}{Colors.ENDC}\n")

def print_success(text):
    print(f"{Colors.OKGREEN}✓ {text}{Colors.ENDC}")

def print_error(text):
    print(f"{Colors.FAIL}✗ {text}{Colors.ENDC}")

def print_info(text):
    print(f"{Colors.OKCYAN}→ {text}{Colors.ENDC}")

def print_warning(text):
    print(f"{Colors.WARNING}⚠ {text}{Colors.ENDC}")

# ============================================================================
# FUNCIONES DE VERIFICACIÓN
# ============================================================================

def check_java():
    """Verifica que Java esté instalado"""
    print_info("Verificando Java...")
    try:
        result = subprocess.run(["java", "-version"], 
                              capture_output=True, 
                              text=True, 
                              check=True)
        version_info = result.stderr.split('\n')[0]
        print_success(f"Java encontrado: {version_info}")
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        print_error("Java no encontrado. Por favor instala JDK 17 o superior.")
        return False

def find_eclipse_maven():
    """Busca Maven embebido de Eclipse"""
    # Ubicaciones comunes de Eclipse + la específica del usuario
    eclipse_paths = [
        r"C:\Users\usuario\eclipse\java-2025-06",  # Ruta específica del usuario
        r"C:\eclipse",
        r"C:\Program Files\Eclipse",
        r"C:\Program Files (x86)\Eclipse",
        os.path.expanduser(r"~\eclipse"),
        os.path.expanduser(r"~\Downloads\eclipse"),
    ]
    
    for eclipse_path in eclipse_paths:
        if os.path.exists(eclipse_path):
            print_info(f"Encontrado Eclipse en: {eclipse_path}")
            
            # Buscar Maven en plugins de Eclipse
            plugins_dir = Path(eclipse_path) / "plugins"
            if plugins_dir.exists():
                # Buscar el plugin de Maven runtime
                maven_runtime_dirs = list(plugins_dir.glob("org.eclipse.m2e.maven.runtime_*"))
                
                if maven_runtime_dirs:
                    # Usar el primero encontrado
                    maven_runtime = maven_runtime_dirs[0]
                    maven_home = maven_runtime / "maven"
                    
                    if maven_home.exists():
                        maven_bin = maven_home / "bin" / "mvn.cmd"
                        if maven_bin.exists():
                            print_success(f"Maven de Eclipse encontrado: {maven_bin}")
                            return str(maven_bin)
                    
                    # Alternativa: buscar en jars
                    maven_jars = maven_runtime / "jars"
                    if maven_jars.exists():
                        print_info(f"Maven runtime encontrado en: {maven_jars}")
                        return str(maven_runtime)
    
    return None

def check_maven():
    """Verifica que Maven esté instalado o usa el de Eclipse"""
    print_info("Verificando Maven...")
    
    # Primero intentar Maven del sistema
    try:
        result = subprocess.run(["mvn", "-version"], 
                              capture_output=True, 
                              text=True, 
                              check=True)
        version_info = result.stdout.split('\n')[0]
        print_success(f"Maven encontrado en el sistema: {version_info}")
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        print_warning("Maven no encontrado en PATH del sistema")
    
    # Intentar Maven de Eclipse
    eclipse_maven = find_eclipse_maven()
    if eclipse_maven:
        print_success(f"Maven de Eclipse encontrado: {eclipse_maven}")
        return True
    
    # Usar Maven embebido en el workspace de Eclipse
    workspace_maven = PROJECT_DIR.parent.parent / "maven"
    if workspace_maven.exists():
        print_success(f"Maven del workspace encontrado: {workspace_maven}")
        return True
    
    print_warning("Maven no encontrado, pero Eclipse puede tenerlo integrado")
    print_info("Continuando... Eclipse ejecutará Maven internamente")
    return True  # Continuar de todas formas, Eclipse lo manejará

def check_jpackage():
    """Verifica que jpackage esté disponible"""
    print_info("Verificando jpackage...")
    try:
        result = subprocess.run(["jpackage", "--version"], 
                              capture_output=True, 
                              text=True, 
                              check=True)
        print_success(f"jpackage encontrado: {result.stdout.strip()}")
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        print_error("jpackage no encontrado. Requiere JDK 14 o superior.")
        return False

# ============================================================================
# FUNCIONES DE CONSTRUCCIÓN
# ============================================================================

def clean_directories():
    """Limpia directorios de construcción anteriores"""
    print_info("Limpiando directorios anteriores...")

    # Intentar cerrar la app si está en ejecución para evitar bloqueo de archivos
    try:
        subprocess.run(["taskkill", "/F", "/IM", f"{APP_NAME.replace(' ', '')}.exe"],
                       capture_output=True, text=True)
    except Exception:
        pass

    def _on_rm_error(func, path, exc_info):
        try:
            import stat
            os.chmod(path, stat.S_IWRITE)
            try:
                if os.path.isdir(path):
                    os.rmdir(path)
                else:
                    os.remove(path)
            except Exception:
                # Ignorar si no se puede eliminar (probablemente en uso)
                print_warning(f"No se pudo eliminar: {path}")
        except Exception:
            print_warning(f"No se pudo ajustar permisos: {path}")

    if BUILD_DIR.exists():
        try:
            shutil.rmtree(BUILD_DIR, onerror=_on_rm_error)
            print_success(f"Eliminado: {BUILD_DIR}")
        except Exception as e:
            print_warning(f"No se pudo eliminar {BUILD_DIR}: {e}")

    if OUTPUT_DIR.exists():
        try:
            shutil.rmtree(OUTPUT_DIR, onerror=_on_rm_error)
            print_success(f"Eliminado: {OUTPUT_DIR}")
        except Exception as e:
            print_warning(f"No se pudo eliminar {OUTPUT_DIR}: {e}")
    
    # Crear directorios
    BUILD_DIR.mkdir(parents=True, exist_ok=True)
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    print_success("Directorios creados")

def compile_project():
    """Compila el proyecto con Maven (usando Eclipse o sistema)"""
    print_info("Compilando proyecto con Maven...")
    
    # Método 1: Intentar mvn del sistema
    try:
        result = subprocess.run(
            ["mvn", "clean", "package", "-DskipTests"],
            cwd=PROJECT_DIR,
            capture_output=True,
            text=True,
            check=True
        )
        
        jar_path = TARGET_DIR / MAIN_JAR
        if jar_path.exists():
            print_success(f"Proyecto compilado con Maven del sistema: {jar_path}")
            return True
            
    except (subprocess.CalledProcessError, FileNotFoundError) as e:
        print_warning("Maven del sistema no disponible, buscando Maven de Eclipse...")
    
    # Método 2: Buscar y usar Maven de Eclipse
    eclipse_maven = find_eclipse_maven()
    if eclipse_maven and eclipse_maven.endswith("mvn.cmd"):
        print_info(f"Intentando compilar con Maven de Eclipse: {eclipse_maven}")
        try:
            result = subprocess.run(
                [str(eclipse_maven), "clean", "package", "-DskipTests"],
                cwd=PROJECT_DIR,
                capture_output=True,
                text=True,
                check=True,
                shell=True
            )
            
            jar_path = TARGET_DIR / MAIN_JAR
            if jar_path.exists():
                print_success(f"Proyecto compilado con Maven de Eclipse: {jar_path}")
                return True
        except subprocess.CalledProcessError as e:
            print_warning(f"Maven de Eclipse falló: {e}")
            print_info("Salida: " + (e.stdout or ""))
            print_info("Error: " + (e.stderr or ""))
    
    # Método 3: Usar Maven wrapper si existe
    mvnw_cmd = PROJECT_DIR / "mvnw.cmd"
    if mvnw_cmd.exists():
        print_info("Intentando con Maven wrapper...")
        try:
            result = subprocess.run(
                [str(mvnw_cmd), "clean", "package", "-DskipTests"],
                cwd=PROJECT_DIR,
                capture_output=True,
                text=True,
                check=True
            )
            
            jar_path = TARGET_DIR / MAIN_JAR
            if jar_path.exists():
                print_success(f"Proyecto compilado con Maven wrapper: {jar_path}")
                return True
                
        except subprocess.CalledProcessError as e:
            print_warning("Maven wrapper falló, intentando alternativa...")
    
    # Método 4: Compilar con Eclipse directamente (si está disponible)
    eclipse_exe = find_eclipse_executable()
    if eclipse_exe:
        print_info("Intentando compilar con Eclipse...")
        try:
            # Eclipse puede compilar desde línea de comandos
            result = subprocess.run(
                [eclipse_exe, "-nosplash", "-application", 
                 "org.eclipse.jdt.apt.core.aptBuild",
                 "-data", str(PROJECT_DIR.parent.parent)],
                cwd=PROJECT_DIR,
                capture_output=True,
                text=True,
                timeout=120
            )
            
            jar_path = TARGET_DIR / MAIN_JAR
            if jar_path.exists():
                print_success(f"Proyecto compilado con Eclipse: {jar_path}")
                return True
        except (subprocess.CalledProcessError, subprocess.TimeoutExpired):
            print_warning("Eclipse build falló")
    
    # Método 5: Verificar si ya está compilado
    jar_path = TARGET_DIR / MAIN_JAR
    if jar_path.exists():
        print_warning("No se pudo ejecutar Maven, pero el JAR ya existe")
        # Intentar recompilar fuentes para incluir cambios recientes
        print_info("Intentando actualizar clases con javac y regenerar JAR...")
        if compile_with_javac():
            if build_jar_from_target():
                print_success(f"JAR actualizado: {jar_path}")
                return True
        # Si no se puede recompilar, usar el JAR existente
        print_success(f"Usando JAR existente: {jar_path}")
        return True
    
    # Si nada funcionó
    # Intento final: compilar con javac y crear el JAR
    print_warning("Intentando compilar con javac y crear JAR sin Maven...")
    if compile_with_javac():
        if build_jar_from_target():
            print_success(f"JAR creado sin Maven: {TARGET_DIR / MAIN_JAR}")
            return True

    print_error(f"No se pudo compilar el proyecto ni crear el JAR")
    print_header("⚠️  ACCIÓN REQUERIDA: Compilar en Eclipse")
    print_info("Sigue estos pasos en Eclipse:")
    print_info("")
    print_info("  1. Abre Eclipse con el proyecto 'todo.crud.list'")
    print_info("  2. Click DERECHO en el proyecto en el Package Explorer")
    print_info("  3. Selecciona: Run As → Maven build...")
    print_info("  4. En la ventana que aparece:")
    print_info("     - Goals: clean package")
    print_info("     - (Opcional) Marca: Skip Tests")
    print_info("  5. Click en 'Run'")
    print_info("  6. Espera a ver 'BUILD SUCCESS' en la consola")
    print_info("  7. Verifica que exista: target/todo.crud.list-0.0.1-SNAPSHOT.jar")
    print_info("  8. Ejecuta este script de nuevo")
    print_info("")
    print_warning("Archivo esperado: " + str(TARGET_DIR / MAIN_JAR))
    return False

def find_eclipse_executable():
    """Busca el ejecutable de Eclipse"""
    eclipse_paths = [
        r"C:\eclipse\eclipse.exe",
        r"C:\Program Files\Eclipse\eclipse.exe",
        r"C:\Program Files (x86)\Eclipse\eclipse.exe",
        os.path.expanduser(r"~\eclipse\eclipse.exe"),
    ]
    
    for path in eclipse_paths:
        if os.path.exists(path):
            return path
    return None

def copy_dependencies():
    """Copia el JAR y dependencias al directorio de build"""
    print_info("Copiando dependencias...")
    
    # Copiar JAR principal
    jar_src = TARGET_DIR / MAIN_JAR
    jar_dest = BUILD_DIR / MAIN_JAR
    
    if jar_src.exists():
        shutil.copy2(jar_src, jar_dest)
        print_success(f"JAR copiado: {MAIN_JAR}")
    else:
        print_error(f"JAR no encontrado: {jar_src}")
        return False
    
    # Crear directorio libs
    libs_dir = BUILD_DIR / "libs"
    libs_dir.mkdir(exist_ok=True)
    
    # Copiar dependencias - Método 1: Maven
    try:
        subprocess.run(
            ["mvn", "dependency:copy-dependencies", 
             f"-DoutputDirectory={libs_dir}"],
            cwd=PROJECT_DIR,
            check=True,
            capture_output=True,
            timeout=60
        )
        print_success("Dependencias copiadas con Maven")
        return True
    except (subprocess.CalledProcessError, FileNotFoundError, subprocess.TimeoutExpired):
        print_warning("Maven no disponible para copiar dependencias")
    
    # Método 2: Copiar desde .m2 repository de Eclipse
    m2_repo = Path.home() / ".m2" / "repository"
    if m2_repo.exists():
        print_info("Buscando dependencias en repositorio local .m2...")
        
        # Dependencias conocidas del pom.xml
        dependencies = [
            ("org/openjfx/javafx-controls", "21.0.1"),
            ("org/openjfx/javafx-fxml", "21.0.1"),
            ("org/openjfx/javafx-graphics", "21.0.1"),
            ("org/openjfx/javafx-base", "21.0.1"),
            ("com/google/code/gson/gson", "2.10.1"),
        ]
        
        copied = 0
        for dep_path, version in dependencies:
            dep_dir = m2_repo / dep_path / version
            if dep_dir.exists():
                for jar_file in dep_dir.glob("*.jar"):
                    if not jar_file.name.endswith("-sources.jar") and \
                       not jar_file.name.endswith("-javadoc.jar"):
                        shutil.copy2(jar_file, libs_dir)
                        copied += 1
                        print_info(f"  Copiado: {jar_file.name}")
        
        if copied > 0:
            print_success(f"Copiadas {copied} dependencias desde repositorio local")
            return True
    
    # Método 3: Verificar si ya existen en target
    target_libs = TARGET_DIR / "lib"
    if target_libs.exists():
        for jar_file in target_libs.glob("*.jar"):
            shutil.copy2(jar_file, libs_dir)
        print_success("Dependencias copiadas desde target/lib")
        return True
    
    print_warning("No se pudieron copiar todas las dependencias")
    print_info("La aplicación podría requerir JavaFX instalado en el sistema")
    return True  # Continuar de todas formas

def compile_with_javac():
    """Compila las fuentes Java con javac usando dependencias de ~/.m2."""
    try:
        src_java_dir = PROJECT_DIR / "src" / "main" / "java"
        sources = [str(p) for p in src_java_dir.rglob("*.java")]
        if not sources:
            print_warning("No se encontraron fuentes .java para compilar")
            return False

        classes_out = TARGET_DIR / "classes"
        classes_out.mkdir(parents=True, exist_ok=True)

        # Construir classpath con dependencias conocidas
        m2_repo = Path.home() / ".m2" / "repository"
        dep_coords = [
            ("org/openjfx/javafx-controls", "21.0.1"),
            ("org/openjfx/javafx-fxml", "21.0.1"),
            ("org/openjfx/javafx-graphics", "21.0.1"),
            ("org/openjfx/javafx-base", "21.0.1"),
            ("com/google/code/gson/gson", "2.10.1"),
        ]
        cp_jars = []
        for path, ver in dep_coords:
            dep_dir = m2_repo / path / ver
            if dep_dir.exists():
                for jar in dep_dir.glob("*.jar"):
                    if not jar.name.endswith("-sources.jar") and not jar.name.endswith("-javadoc.jar"):
                        cp_jars.append(str(jar))

        # Incluir clases ya compiladas en classpath
        cp_jars.append(str(classes_out))

        classpath = ";".join(cp_jars) if cp_jars else ""

        cmd = [
            "javac",
            "-encoding", "UTF-8",
            "-d", str(classes_out),
        ]
        if classpath:
            cmd.extend(["-cp", classpath])
        cmd.extend(sources)

        print_info("Compilando con javac...")
        result = subprocess.run(cmd, capture_output=True, text=True)
        if result.returncode != 0:
            print_error("Error compilando con javac")
            print(result.stdout)
            print(result.stderr)
            return False
        print_success("Fuentes compiladas con javac")
        return True
    except Exception as e:
        print_error(f"Fallo al compilar con javac: {e}")
        return False

def build_jar_from_target():
    """Construye un JAR ejecutable usando clases y recursos en target/ sin Maven."""
    try:
        classes_dir = TARGET_DIR / "classes"
        fxml_dir = TARGET_DIR / "fxml"
        styles_dir = TARGET_DIR / "styles"
        # Fallback: usar src/main/resources si target no tiene recursos
        src_resources = PROJECT_DIR / "src" / "main" / "resources"
        jar_path = TARGET_DIR / MAIN_JAR

        if not classes_dir.exists():
            print_error(f"No existe {classes_dir}. Compila el proyecto en Eclipse primero.")
            return False

        # Crear estructura de JAR (ZIP) con MANIFEST
        import zipfile
        jar_path.parent.mkdir(parents=True, exist_ok=True)
        with zipfile.ZipFile(jar_path, 'w', compression=zipfile.ZIP_DEFLATED) as jar:
            # MANIFEST con Main-Class
            manifest_content = (
                "Manifest-Version: 1.0\n"
                f"Main-Class: {MAIN_CLASS}\n\n"
            )
            jar.writestr('META-INF/MANIFEST.MF', manifest_content)

            # Añadir clases compiladas
            for root, _, files in os.walk(classes_dir):
                for f in files:
                    src = Path(root) / f
                    # Ruta relativa dentro del JAR
                    rel = src.relative_to(classes_dir)
                    jar.writestr(str(rel).replace('\\', '/'), src.read_bytes())

            # Añadir recursos fxml y styles si existen
            if not fxml_dir.exists() and (src_resources / "fxml").exists():
                fxml_dir = src_resources / "fxml"
            if fxml_dir.exists():
                for root, _, files in os.walk(fxml_dir):
                    for f in files:
                        src = Path(root) / f
                        rel = src.relative_to(fxml_dir)
                        jar.writestr(f"fxml/{str(rel).replace('\\', '/')}", src.read_bytes())
            if not styles_dir.exists() and (src_resources / "styles").exists():
                styles_dir = src_resources / "styles"
            if styles_dir.exists():
                for root, _, files in os.walk(styles_dir):
                    for f in files:
                        src = Path(root) / f
                        rel = src.relative_to(styles_dir)
                        jar.writestr(f"styles/{str(rel).replace('\\', '/')}", src.read_bytes())

        print_success(f"JAR construido: {jar_path}")
        return True
    except Exception as e:
        print_error(f"Error creando JAR sin Maven: {e}")
        return False

def create_launcher_script():
    """Crea script de lanzamiento .vbs (sin ventana CMD)"""
    print_info("Creando script de lanzamiento...")
    
    # Crear launcher VBS que no muestra ventana de CMD
    vbs_launcher_content = f"""' Launcher for {APP_NAME}
' Generated automatically - No CMD window
Set objShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

' Obtener directorio del script
scriptDir = fso.GetParentFolderName(WScript.ScriptFullName)
jarPath = scriptDir & "\\{MAIN_JAR}"

' Ejecutar sin mostrar ventana (usar Chr(34) para comillas)
objShell.Run "javaw -jar " & Chr(34) & jarPath & Chr(34), 0, False

Set objShell = Nothing
Set fso = Nothing
"""
    
    vbs_launcher_path = BUILD_DIR / "launch.vbs"
    vbs_launcher_path.write_text(vbs_launcher_content, encoding='utf-8')
    print_success(f"Script VBS creado (sin ventana CMD): {vbs_launcher_path.name}")
    
    # Crear también un .bat como respaldo (por si alguien lo prefiere)
    bat_launcher_content = f"""@echo off
REM Launcher for {APP_NAME}
REM Generated automatically
REM Usa javaw en lugar de java para evitar ventana de CMD

start javaw -jar "%~dp0{MAIN_JAR}"
"""
    
    bat_launcher_path = BUILD_DIR / "launch.bat"
    bat_launcher_path.write_text(bat_launcher_content, encoding='utf-8')
    print_success(f"Script BAT creado (respaldo): {bat_launcher_path.name}")

    # Copiar logo.png al directorio de build para que el ejecutable pueda usarlo como fallback
    project_logo = PROJECT_DIR / "logo.png"
    if project_logo.exists():
        shutil.copy2(project_logo, BUILD_DIR / "logo.png")
        print_success("logo.png copiado al build (para icono de tarea)")

def create_jpackage_exe():
    """Crea ejecutable con jpackage"""
    print_info("Creando ejecutable con jpackage...")
    
    # Icono para el .exe si está disponible
    icon_path = PROJECT_DIR / "icon.ico"
    has_icon = icon_path.exists()

    # Comando jpackage
    cmd = [
        "jpackage",
        "--type", "app-image",  # Solo crear imagen, no instalador todavía
        "--input", str(BUILD_DIR),
        # Generar la imagen en 'output' para rutas más cortas y limpias
        "--dest", str(OUTPUT_DIR),
        "--name", APP_NAME.replace(" ", ""),
        "--main-jar", MAIN_JAR,
        "--main-class", MAIN_CLASS,
        "--app-version", APP_VERSION,
        "--vendor", APP_VENDOR,
        "--description", APP_DESCRIPTION,
        "--java-options", "-Xmx512m",
        # Incluir módulos JavaFX desde los JARs modulares de libs
        "--module-path", str(BUILD_DIR / "libs"),
        "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics,javafx.base",
        # NO usar --win-console para evitar ventana de CMD
    ]

    if has_icon:
        cmd.extend(["--icon", str(icon_path)])
    
    try:
        print_info(f"Ejecutando: {' '.join(cmd)}")
        result = subprocess.run(
            cmd,
            cwd=PROJECT_DIR,
            capture_output=True,
            text=True,
            check=True
        )
        print_success("Ejecutable creado con jpackage")
        # Copiar logo.png también a la carpeta bin del app-image para que el EXE la encuentre
        try:
            app_dir_name = APP_NAME.replace(" ", "")
            app_root = OUTPUT_DIR / app_dir_name
            bin_dir = app_root / "bin"
            project_logo = PROJECT_DIR / "logo.png"
            if project_logo.exists():
                shutil.copy2(project_logo, app_root / "logo.png")
                if bin_dir.exists():
                    shutil.copy2(project_logo, bin_dir / "logo.png")
                print_success("logo.png copiado al app-image (app y bin)")
        except Exception as e:
            print_warning(f"No se pudo copiar logo.png al app-image: {e}")
        return True
    except subprocess.CalledProcessError as e:
        print_error(f"Error al crear ejecutable: {e}")
        print(e.stdout)
        print(e.stderr)
        return False

def create_inno_setup_script():
    """Crea script de Inno Setup"""
    print_info("Creando script de Inno Setup...")
    
    app_exe_name = APP_NAME.replace(" ", "") + ".exe"
    app_dir_name = APP_NAME.replace(" ", "")
    # Preferir la imagen creada por jpackage en OUTPUT; si no existe, usar BUILD
    jpackage_app_dir = OUTPUT_DIR / app_dir_name
    source_dir = jpackage_app_dir if jpackage_app_dir.exists() else BUILD_DIR
    
    # Verificar si existe icon.ico o logo.png
    icon_file = PROJECT_DIR / "icon.ico"
    logo_file = PROJECT_DIR / "logo.png"
    
    if icon_file.exists():
        icon_path = str(icon_file)
        print_success(f"Usando icono: {icon_file.name}")
    elif logo_file.exists():
        print_warning(f"logo.png encontrado pero necesita convertirse a .ico")
        print_info("El instalador usará icono por defecto temporalmente")
        icon_path = None
    else:
        print_warning("Sin icono personalizado - usando icono por defecto")
        icon_path = None
    
    # Configurar línea de SetupIconFile
    setup_icon_line = f"SetupIconFile={icon_path}" if icon_path else "; SetupIconFile not set - using default"
    
    inno_script = f"""
; Script generado automáticamente para {APP_NAME}
; Instalador tipo "Next, Next, Install" para Windows 11

#define MyAppName "{APP_NAME}"
#define MyAppVersion "{APP_VERSION}"
#define MyAppPublisher "{APP_VENDOR}"
#define MyAppExeName "{app_exe_name}"
#define MyAppDescription "{APP_DESCRIPTION}"

[Setup]
; Información de la aplicación
AppId={{{{8F3E5A21-9B7C-4D6E-A123-45678901234F}}}}
AppName={{#MyAppName}}
AppVersion={{#MyAppVersion}}
AppPublisher={{#MyAppPublisher}}
AppPublisherURL=https://github.com/inforgonzalez
AppSupportURL=https://github.com/inforgonzalez
AppUpdatesURL=https://github.com/inforgonzalez
DefaultDirName={{autopf}}\\{{#MyAppName}}
DefaultGroupName={{#MyAppName}}
AllowNoIcons=yes
LicenseFile={PROJECT_DIR}\\LICENSE.txt
InfoBeforeFile={PROJECT_DIR}\\README.md
OutputDir={OUTPUT_DIR}
OutputBaseFilename=TODO_CRUD_List_Setup_v{APP_VERSION}
{setup_icon_line}
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=lowest
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64

; Estilo por defecto (sin imágenes personalizadas por compatibilidad)

[Languages]
Name: "spanish"; MessagesFile: "compiler:Languages\\Spanish.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{{cm:CreateDesktopIcon}}"; GroupDescription: "{{cm:AdditionalIcons}}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{{cm:CreateQuickLaunchIcon}}"; GroupDescription: "{{cm:AdditionalIcons}}"; Flags: unchecked; OnlyBelowVersion: 6.1; Check: not IsAdminInstallMode

[Files]
Source: "{source_dir}\\*"; DestDir: "{{app}}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{{group}}\\{{#MyAppName}}"; Filename: "{{app}}\\{{#MyAppExeName}}"
Name: "{{group}}\\{{cm:UninstallProgram,{{#MyAppName}}}}"; Filename: "{{uninstallexe}}"
Name: "{{autodesktop}}\\{{#MyAppName}}"; Filename: "{{app}}\\{{#MyAppExeName}}"; Tasks: desktopicon
Name: "{{userappdata}}\\Microsoft\\Internet Explorer\\Quick Launch\\{{#MyAppName}}"; Filename: "{{app}}\\{{#MyAppExeName}}"; Tasks: quicklaunchicon

[Run]
Filename: "{{app}}\\{{#MyAppExeName}}"; Description: "{{cm:LaunchProgram,{{#StringChange(MyAppName, '&', '&&')}}}}"; Flags: nowait postinstall skipifsilent

[Code]
// Verificar si Java está instalado
function IsJavaInstalled(): Boolean;
var
  ResultCode: Integer;
begin
  Result := Exec('java', '-version', '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
end;

function InitializeSetup(): Boolean;
begin
  Result := True;
  if not IsJavaInstalled() then
  begin
    MsgBox('Java Runtime Environment (JRE) no está instalado.' + #13#10 + 
           'Por favor instale Java 17 o superior antes de instalar esta aplicación.' + #13#10 + #13#10 +
           'Puede descargarlo desde: https://adoptium.net/', mbError, MB_OK);
    Result := False;
  end;
end;
"""
    
    inno_path = INSTALLER_DIR / "setup_script.iss"
    inno_path.write_text(inno_script, encoding='utf-8')
    print_success(f"Script de Inno Setup creado: {inno_path.name}")
    
    # Información adicional sobre el logo
    if not icon_file.exists() and logo_file.exists():
        print_info("")
        print_info("💡 TIP: Para usar tu logo.png en el instalador:")
        print_info("   Opción 1: Instala Pillow con: pip install Pillow")
        print_info("            Luego ejecuta este script de nuevo")
        print_info("   Opción 2: Convierte logo.png a icon.ico manualmente")
        print_info("            Usa: https://convertio.co/png-ico/")
        print_info("")
    
    return inno_path

def create_basic_files():
    """Crea archivos básicos si no existen"""
    print_info("Verificando archivos necesarios...")
    
    # LICENSE.txt
    license_path = PROJECT_DIR / "LICENSE.txt"
    if not license_path.exists():
        license_path.write_text(f"""MIT License

Copyright (c) 2026 {APP_VENDOR}

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
""", encoding='utf-8')
        print_success("LICENSE.txt creado")
    
    # Convertir logo.png a icon.ico si existe
    logo_png = PROJECT_DIR / "logo.png"
    icon_ico = PROJECT_DIR / "icon.ico"
    
    if logo_png.exists() and not icon_ico.exists():
        print_info("Convirtiendo logo.png a icon.ico...")
        try:
            # Intentar con PIL/Pillow
            from PIL import Image
            
            img = Image.open(logo_png)
            # Redimensionar a tamaños estándar de icono
            icon_sizes = [(16, 16), (32, 32), (48, 48), (64, 64), (128, 128), (256, 256)]
            img.save(icon_ico, format='ICO', sizes=icon_sizes)
            print_success(f"Icono creado: icon.ico desde logo.png")
        except ImportError:
            print_warning("PIL/Pillow no instalado, no se puede convertir logo.png a .ico")
            print_info("Instala con: pip install Pillow")
            print_info("O usa un conversor online: https://convertio.co/png-ico/")
        except Exception as e:
            print_warning(f"No se pudo convertir logo.png: {e}")
    
    # Verificar icon.ico
    if icon_ico.exists():
        print_success(f"Icono encontrado: icon.ico")
    elif logo_png.exists():
        print_info(f"Logo encontrado: logo.png (se usará si se convierte a .ico)")
    else:
        print_warning("Ni icon.ico ni logo.png encontrados - el instalador usará icono por defecto")

def patch_jar_css():
    """Parchea el CSS dentro del/los JAR(es) para mejorar el resaltado de selección.
    Aplica sobre JAR en target y en build si existen.
    """
    print_info("Parcheando CSS dentro del JAR para mejorar selección...")
    jar_paths = [TARGET_DIR / MAIN_JAR, BUILD_DIR / MAIN_JAR]

    import zipfile
    from tempfile import NamedTemporaryFile

    override_css = (
        "\n/* --- Tema claro: textos oscuros y selección suave --- */\n"
        ".root { -fx-background-color: #ffffff; -fx-text-fill: #1a1a1a; }\n"
        ".label, .text, .text-field, .text-area { -fx-text-fill: #1a1a1a; }\n"
        ".table-view, .list-view, .combo-box, .table-cell { -fx-background-color: #ffffff; -fx-text-fill: #1a1a1a; }\n"
        ".table-view .column-header-background { -fx-background-color: #f3f3f3; }\n"
        ".table-view .table-column .label { -fx-text-fill: #1a1a1a; }\n"
        ".table-view:focused .table-row-cell:selected,\n"
        ".table-view .table-row-cell:selected {\n"
        "    -fx-background-color: #E5F0FB;\n"
        "    -fx-text-fill: #1a1a1a;\n"
        "}\n"
        ".table-row-cell.selected .table-cell,\n"
        ".table-row-cell:selected .table-cell { -fx-text-fill: #1a1a1a; }\n"
        ".table-row-cell:selected .text { -fx-fill: #1a1a1a; }\n"
        ".table-view .table-row-cell:hover { -fx-background-color: #f6faff; }\n"
        "/* ComboBox/ListView item seleccionado */\n"
        ".list-view .list-cell:selected,\n"
        ".combo-box .list-cell:selected {\n"
        "    -fx-background-color: #E5F0FB;\n"
        "    -fx-text-fill: #1a1a1a;\n"
        "}\n"
    )

    success_any = False
    for jar_path in jar_paths:
        if not jar_path.exists():
            continue
        try:
            with NamedTemporaryFile(delete=False, suffix=".jar") as tmp_file:
                tmp_path = Path(tmp_file.name)
            # Crear jar nuevo copiando entradas del original, sobrescribiendo el CSS
            with zipfile.ZipFile(jar_path, 'r') as zin, zipfile.ZipFile(tmp_path, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
                css_entry = 'styles/application.css'
                had_css = False
                for item in zin.infolist():
                    data = zin.read(item.filename)
                    if item.filename == css_entry:
                        had_css = True
                        try:
                            # Añadir las reglas de selección al final del CSS existente
                            data = data + override_css.encode('utf-8')
                        except Exception:
                            # Si falla, usar solo el override
                            data = override_css.encode('utf-8')
                    zout.writestr(item, data)
                if not had_css:
                    # Si el css no existía, agregarlo
                    zout.writestr(css_entry, override_css.encode('utf-8'))
            # Reemplazar jar original
            shutil.move(str(tmp_path), str(jar_path))
            print_success(f"CSS del JAR parcheado: {jar_path}")
            success_any = True
        except Exception as e:
            print_warning(f"No se pudo parchear CSS en {jar_path}: {e}")
    return success_any

def compile_inno_setup(script_path):
    """Compila el script de Inno Setup"""
    print_info("Compilando instalador con Inno Setup...")
    
    # Buscar Inno Setup en ubicaciones comunes
    inno_paths = [
        r"C:\Program Files (x86)\Inno Setup 6\ISCC.exe",
        r"C:\Program Files\Inno Setup 6\ISCC.exe",
        r"C:\Program Files (x86)\Inno Setup 5\ISCC.exe",
        r"C:\Program Files\Inno Setup 5\ISCC.exe",
    ]
    
    iscc_exe = None
    for path in inno_paths:
        if Path(path).exists():
            iscc_exe = path
            break
    
    if not iscc_exe:
        print_error("Inno Setup no encontrado.")
        print_info("Por favor instala Inno Setup desde: https://jrsoftware.org/isdl.php")
        print_info(f"Puedes compilar manualmente el script: {script_path}")
        return False
    
    try:
        result = subprocess.run(
            [iscc_exe, str(script_path)],
            capture_output=True,
            text=True,
            check=True
        )
        print_success("Instalador compilado con éxito!")
        return True
    except subprocess.CalledProcessError as e:
        print_error(f"Error al compilar instalador: {e}")
        print(e.stdout)
        print(e.stderr)
        return False

# ============================================================================
# FUNCIÓN PRINCIPAL
# ============================================================================

def main():
    """Función principal"""
    print_header(f"Creador de Instalador - {APP_NAME}")
    print_info(f"Versión: {APP_VERSION}")
    print_info(f"Directorio: {PROJECT_DIR}")
    
    # 1. Verificaciones
    print_header("Paso 1: Verificando requisitos")
    if not check_java():
        return 1
    
    # Maven es opcional si usamos Eclipse
    maven_available = check_maven()
    if not maven_available:
        print_info("Maven no disponible, pero se intentará usar Eclipse")
    
    # jpackage es opcional, podemos crear instalador sin él
    has_jpackage = check_jpackage()
    
    # 2. Limpiar
    print_header("Paso 2: Limpiando directorios")
    clean_directories()
    
    # 3. Compilar
    print_header("Paso 3: Compilando proyecto")
    if not compile_project():
        return 1
    
    # 4. Copiar dependencias
    print_header("Paso 4: Copiando archivos")
    if not copy_dependencies():
        return 1
    create_launcher_script()
    create_basic_files()
    # Parchear CSS dentro del JAR para mejorar selección visual
    patch_jar_css()

    # Insertar icon.png dentro del JAR para el icono de la barra de tareas
    patch_jar_icon()

    # 5. Crear ejecutable (opcional con jpackage)
    if has_jpackage:
        print_header("Paso 5: Creando ejecutable")
        if not create_jpackage_exe():
            print_warning("No se pudo crear ejecutable con jpackage, continuando...")
    else:
        print_warning("Saltando creación de ejecutable (jpackage no disponible)")

    # 6. Crear script de Inno Setup
    print_header("Paso 6: Creando script de instalador")
    inno_script_path = create_inno_setup_script()

    # 7. Compilar instalador
    print_header("Paso 7: Compilando instalador")
    if compile_inno_setup(inno_script_path):
        print_header("¡ÉXITO!")
        print_success(f"Instalador creado en: {OUTPUT_DIR}")

        # Listar archivos creados
        if OUTPUT_DIR.exists():
            installers = list(OUTPUT_DIR.glob("*.exe"))
            if installers:
                print_info("\nInstaladores generados:")
                for installer in installers:
                    size_mb = installer.stat().st_size / (1024 * 1024)
                    print(f"  📦 {installer.name} ({size_mb:.2f} MB)")
    else:
        print_warning("\nInstalador no compilado automáticamente.")
        print_info(f"Puedes compilar manualmente:")
        print_info(f"  1. Abre Inno Setup")
        print_info(f"  2. Abre el archivo: {inno_script_path}")
        print_info(f"  3. Click en 'Compile' o presiona F9")

    print_header("Proceso completado")
    return 0

def patch_jar_icon():
    """Inserta un icono PNG en el JAR para que App.java pueda cargarlo
    desde el classpath con getResourceAsStream("/icon.png")."""
    print_info("Insertando icon.png dentro del JAR...")
    jar_paths = [TARGET_DIR / MAIN_JAR, BUILD_DIR / MAIN_JAR]

    # Seleccionar fuente del icono: icon.png preferido, sino logo.png
    icon_src = None
    candidate_icon = PROJECT_DIR / "icon.png"
    if candidate_icon.exists():
        icon_src = candidate_icon
    else:
        candidate_logo = PROJECT_DIR / "logo.png"
        if candidate_logo.exists():
            icon_src = candidate_logo

    if not icon_src:
        print_warning("No se encontró icon.png ni logo.png en el proyecto")
        return False

    import zipfile
    from tempfile import NamedTemporaryFile

    success_any = False
    for jar_path in jar_paths:
        if not jar_path.exists():
            continue
        try:
            with NamedTemporaryFile(delete=False, suffix=".jar") as tmp_file:
                tmp_path = Path(tmp_file.name)
            with zipfile.ZipFile(jar_path, 'r') as zin, zipfile.ZipFile(tmp_path, 'w', compression=zipfile.ZIP_DEFLATED) as zout:
                # Copiar todas las entradas, reemplazando icon.png si existe
                for item in zin.infolist():
                    data = zin.read(item.filename)
                    if item.filename == 'icon.png':
                        data = icon_src.read_bytes()
                    zout.writestr(item, data)
                # Asegurar que icon.png esté presente en el JAR (en raíz del classpath)
                existing_entries = {i.filename for i in zin.infolist()}
                if 'icon.png' not in existing_entries:
                    zout.writestr('icon.png', icon_src.read_bytes())
            shutil.move(str(tmp_path), str(jar_path))
            # Verificar que icon.png exista en el JAR resultante
            try:
                with zipfile.ZipFile(jar_path, 'r') as zchk:
                    has_icon = any(e.filename == 'icon.png' for e in zchk.infolist())
                if has_icon:
                    print_success(f"icon.png insertado en: {jar_path}")
                else:
                    print_warning(f"icon.png NO aparece en: {jar_path}")
            except Exception:
                print_warning(f"No se pudo verificar icon.png en: {jar_path}")
            success_any = True
        except Exception as e:
            print_warning(f"No se pudo insertar icon.png en {jar_path}: {e}")
    return success_any

if __name__ == "__main__":
    try:
        sys.exit(main())
    except KeyboardInterrupt:
        print_error("\n\nProceso cancelado por el usuario")
        sys.exit(1)
    except Exception as e:
        print_error(f"\n\nError inesperado: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
