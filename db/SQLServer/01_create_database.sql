-- Adatbázis és fájlcsoportok létrehozása
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name='wsstepbystep')
CREATE DATABASE wsstepbystep
	COLLATE SQL_Latin1_General_CP1250_CI_AS;
GO

-- Új adatbázis felhasználó létrehozása
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name='wsstepbystepuser')
CREATE LOGIN wsstepbystepuser WITH PASSWORD='a', DEFAULT_DATABASE=wsstepbystep, CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF;
GO

-- Adatbázis váltás
USE wsstepbystep
GO

-- Tulajdonos beállítása
EXEC dbo.sp_changedbowner @loginame='wsstepbystepuser', @map=false
GO


-- Tranzakció beállítása
ALTER DATABASE wsstepbystep SET ALLOW_SNAPSHOT_ISOLATION ON
GO
ALTER DATABASE wsstepbystep SET READ_COMMITTED_SNAPSHOT ON WITH ROLLBACK IMMEDIATE
GO

-- Adatbázis váltás
USE wsstepbystep
GO

SET NOCOUNT ON
GO
