@rem
@rem Copyright 2020 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@echo off

setlocal enabledelayedexpansion

if "%1"=="" goto print_usage

SET DB=0
SET SERVICES=0
SET MONITORING=0
for %%x in (%*) do (	
	if /I "%%~x"=="--db" (
		set DB=1
	)
	if /I "%%~x"=="--svc" (
		set SERVICES=1
	)
	if /I "%%~x"=="--mon" (
		set MONITORING=1
	)	
	if /I "%%~x"=="--all" (
		set SERVICES=1
		set MONITORING=1
	)	
)

IF !SERVICES!==0 (
	IF !MONITORING!==0 (
		IF !DB!==0 (
			for %%x in (%*) do (	
				docker-compose up -d %%~x
			)
			goto end
		)
	)
)

IF !DB!==1 (
	docker-compose up -d postgres
	docker-compose up -d pgadmin
)

IF !SERVICES!==1 (
	docker-compose up -d postgres
	docker-compose up -d pgadmin
	docker-compose up -d webservice
)

IF !MONITORING!==1 (
	docker-compose up -d prometheus
	docker-compose up -d nodeexporter
	docker-compose up -d cadvisor
	docker-compose up -d grafana
)

goto end

:print_usage
echo usage: coU options container-name
echo   "options"
echo     --db: only database
echo     --svc: only services
echo     --mon: only monitoring
echo     --all: each containers
echo   e.g.: coU --svc
echo         coU postgres

:end_error
endlocal
exit /b 1

:end
endlocal
exit /b 0
