@echo off

set JKSSOURCE=jks
set JKSDEST1=..\..\webservice
set JKSDEST2=..\..\semmi
set JKSDEST3=..\..\semmi
set SITEPATH=\src\main\dist\bin\jks

IF EXIST %JKSDEST1% (
	xcopy %JKSSOURCE% %JKSDEST1%%SITEPATH% /S /E /H /Y
	colorecho "jks copied to %JKSDEST1%%SITEPATH%" 14
)

IF EXIST %JKSDEST2% (
	xcopy %JKSSOURCE% %JKSDEST2%%SITEPATH% /S /E /H /Y
	colorecho "jks copied to %JKSDEST2%%SITEPATH%" 14
)

IF EXIST %JKSDEST3% (
	xcopy %JKSSOURCE% %JKSDEST3%%SITEPATH% /S /E /H /Y
	colorecho "jks copied to %JKSDEST3%%SITEPATH%" 14
)
