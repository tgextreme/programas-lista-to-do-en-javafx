' Launcher for TODO CRUD List
' No muestra ventana de CMD
Set objShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

' Obtener directorio del script
scriptDir = fso.GetParentFolderName(WScript.ScriptFullName)
jarPath = scriptDir & "\target\todo.crud.list-0.0.1-SNAPSHOT.jar"

' Verificar que el JAR existe
If Not fso.FileExists(jarPath) Then
    MsgBox "Error: No se encuentra el archivo JAR." & vbCrLf & vbCrLf & _
           "Por favor, compila el proyecto primero:" & vbCrLf & _
           "1. Abre Eclipse" & vbCrLf & _
           "2. Click derecho en el proyecto" & vbCrLf & _
           "3. Run As -> Maven build..." & vbCrLf & _
           "4. Goals: clean package" & vbCrLf & vbCrLf & _
           "Ruta esperada: " & jarPath, vbCritical, "TODO CRUD List - Error"
    WScript.Quit
End If

' Ejecutar sin mostrar ventana (0 = oculta, False = no esperar)
objShell.Run "javaw -jar """ & jarPath & """", 0, False

Set objShell = Nothing
Set fso = Nothing
