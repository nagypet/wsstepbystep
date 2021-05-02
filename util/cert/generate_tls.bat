@echo off
REM ***************************************************************
REM SERVER 
REM ***************************************************************


IF "%1"=="" (
	SET myworkdir=%CD%
) ELSE (
	SET myworkdir=%~dpf1
)

set TLSDIR=%myworkdir%\tls
echo working dir: %myworkdir%
IF NOT EXIST %TLSDIR% GOTO ERROR


set P12_NAME=%myworkdir%\server-keystore.p12
set CRT_NAME=%TLSDIR%\tls.crt
set KEY_NAME=%TLSDIR%\tls.key

"C:\Program Files\Git\usr\bin\openssl.exe" pkcs12 -in %P12_NAME% -nokeys -out %CRT_NAME%
"C:\Program Files\Git\usr\bin\openssl.exe" pkcs12 -in %P12_NAME% -nocerts -nodes -out %KEY_NAME%

exit /b 0

:ERROR
echo.jks folder does not exist
exit /b 1
