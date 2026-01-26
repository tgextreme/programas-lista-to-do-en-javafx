' Launcher for TODO CRUD List
' Generated automatically - No CMD window
Set objShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

' Obtener directorio del script
scriptDir = fso.GetParentFolderName(WScript.ScriptFullName)
jarPath = scriptDir & "\todo.crud.list-0.0.1-SNAPSHOT.jar"

' Ejecutar sin mostrar ventana (usar Chr(34) para comillas)
objShell.Run "javaw -jar " & Chr(34) & jarPath & Chr(34), 0, False

Set objShell = Nothing
Set fso = Nothing
