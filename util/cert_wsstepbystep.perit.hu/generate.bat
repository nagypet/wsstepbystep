@echo off
REM ***************************************************************
REM SERVER 
REM ***************************************************************


IF "%1"=="" (
	SET myworkdir=%CD%
) ELSE (
	SET myworkdir=%~dpf1
)

set JKSDIR=%myworkdir%\jks
set TLSDIR=%myworkdir%\tls
set TMPDIR=%myworkdir%\tmp
echo working dir: %myworkdir%
IF EXIST %JKSDIR% RMDIR /S /Q %JKSDIR%
IF NOT EXIST %JKSDIR% MD %JKSDIR%
IF EXIST %TLSDIR% RMDIR /S /Q %TLSDIR%
IF NOT EXIST %TLSDIR% MD %TLSDIR%
IF EXIST %TMPDIR% RMDIR /S /Q %TMPDIR%
IF NOT EXIST %TMPDIR% MD %TMPDIR%


set SUBJECT=*.wsstepbystep.perit.hu
set SUBJECT_FILE_NAME=wsstepbystep.perit.hu
set SERVER_STORENAME=%JKSDIR%\server-keystore-%SUBJECT_FILE_NAME%.jks
set CLIENT_STORENAME=%JKSDIR%\client-truststore-%SUBJECT_FILE_NAME%.jks
set P12_NAME=%TMPDIR%\server-keystore-%SUBJECT_FILE_NAME%.p12
set STOREPASS=changeit
set KEYPASS=changeit
set ALIAS=templatekey
set CERTFILE_LOCALHOST=%TMPDIR%\client-truststore-%SUBJECT_FILE_NAME%.cer
set CRT_NAME=%TLSDIR%\tls.crt
set KEY_NAME=%TLSDIR%\tls.key


IF EXIST %SERVER_STORENAME% DEL /F /Q %SERVER_STORENAME% 

rem ============= self signed certificate for localhost
keytool.exe -genkeypair -alias %ALIAS% -keyalg RSA -keysize 2048 -validity 3650 -dname "CN=%SUBJECT%" -keypass %KEYPASS% -keystore %SERVER_STORENAME% -storepass %STOREPASS% -ext san=dns:localhost,dns:keycloak.wsstepbystep.perit.hu,dns:webservice.wsstepbystep.perit.hu,dns:webservice2.wsstepbystep.perit.hu

keytool.exe -exportcert -alias %ALIAS% -file %CERTFILE_LOCALHOST% -keystore %SERVER_STORENAME% -storepass %STOREPASS%
keytool.exe -importcert -keystore %CLIENT_STORENAME% -alias %ALIAS% -file %CERTFILE_LOCALHOST% -storepass %STOREPASS% -noprompt

rem ============= p12
keytool -importkeystore -srckeystore %SERVER_STORENAME% -srcstorepass %STOREPASS% -destkeystore %P12_NAME% -deststoretype PKCS12 -deststorepass %STOREPASS% -noprompt

rem ============= print the content
keytool.exe -list -keystore %SERVER_STORENAME% -storepass %STOREPASS% 

rem ============= key and crt
"C:\Program Files\Git\usr\bin\openssl.exe" pkcs12 -in %P12_NAME% -nokeys -out %CRT_NAME%
"C:\Program Files\Git\usr\bin\openssl.exe" pkcs12 -in %P12_NAME% -nocerts -nodes -out %KEY_NAME%
