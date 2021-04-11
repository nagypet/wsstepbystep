@echo off

set launcher=%TMP%\_launcher.bat

if exist "%launcher%" (
	del /q /f "%launcher%"
)

echo.@echo off>>"%launcher%"
echo.ConsoleSize 200 20 200 9999>>"%launcher%"
echo.cd build\install\webservice\bin>>"%launcher%"
echo.call webservice.bat>>"%launcher%"

start cmd /c "%launcher%"
