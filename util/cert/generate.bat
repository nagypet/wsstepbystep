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
echo working dir: %myworkdir%
IF EXIST %JKSDIR% RMDIR /S /Q %JKSDIR%
IF NOT EXIST %JKSDIR% MD %JKSDIR%
IF EXIST %TLSDIR% RMDIR /S /Q %TLSDIR%
IF NOT EXIST %TLSDIR% MD %TLSDIR%


set SERVER_STORENAME=%JKSDIR%\server-keystore.jks
set CLIENT_STORENAME=%JKSDIR%\client-truststore.jks
set P12_NAME=%myworkdir%\server-keystore.p12
set STOREPASS=changeit
set KEYPASS=changeit
set ALIAS=templatekey
set CERTFILE_LOCALHOST=%myworkdir%\client-truststore.cer

IF EXIST %SERVER_STORENAME% DEL /F /Q %SERVER_STORENAME% 

rem ============= self signed certificate for localhost
keytool.exe -genkeypair -alias %ALIAS% -keyalg RSA -keysize 2048 -validity 3650 -dname "CN=localhost" -keypass %KEYPASS% -keystore %SERVER_STORENAME% -storepass %STOREPASS% -ext san=dns:localhost,dns:host.docker.internal,dns:webservice,dns:webservice2,dns:keycloak

keytool.exe -exportcert -alias %ALIAS% -file %CERTFILE_LOCALHOST% -keystore %SERVER_STORENAME% -storepass %STOREPASS%
keytool.exe -importcert -keystore %CLIENT_STORENAME% -alias %ALIAS% -file %CERTFILE_LOCALHOST% -storepass %STOREPASS% -noprompt

rem ============= p12
keytool -importkeystore -srckeystore %SERVER_STORENAME% -srcstorepass %STOREPASS% -destkeystore %P12_NAME% -deststoretype PKCS12 -deststorepass %STOREPASS% -noprompt

rem ============= print the content
keytool.exe -list -keystore %SERVER_STORENAME% -storepass %STOREPASS% 

