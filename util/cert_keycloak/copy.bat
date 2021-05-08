@echo off

set TLSSOURCE=tls
set TLSDEST1=..\..\keycloak\docker\cert

IF EXIST %TLSDEST1% (
	xcopy %TLSSOURCE% %TLSDEST1% /S /E /H /Y
	colorecho "tls copied to %TLSDEST1%" 14
)
