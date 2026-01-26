
; Script generado automáticamente para TODO CRUD List
; Instalador tipo "Next, Next, Install" para Windows 11

#define MyAppName "TODO CRUD List"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "InfoGonzalez"
#define MyAppExeName "TODOCRUDList.exe"
#define MyAppDescription "Gestor de Tareas con JavaFX"

[Setup]
; Información de la aplicación
AppId={{8F3E5A21-9B7C-4D6E-A123-45678901234F}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL=https://github.com/inforgonzalez
AppSupportURL=https://github.com/inforgonzalez
AppUpdatesURL=https://github.com/inforgonzalez
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
LicenseFile=C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\LICENSE.txt
InfoBeforeFile=C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\README.md
OutputDir=C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\installer\output
OutputBaseFilename=TODO_CRUD_List_Setup_v1.0.0
SetupIconFile=C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\icon.ico
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=lowest
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64

; Estilo por defecto (sin imágenes personalizadas por compatibilidad)

[Languages]
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 6.1; Check: not IsAdminInstallMode

[Files]
Source: "C:\Users\usuario\Workspace Eclipse YouTube\todo.crud.list\installer\output\TODOCRUDList\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

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
