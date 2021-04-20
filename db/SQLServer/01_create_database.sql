-- Adatb�zis �s f�jlcsoportok l�trehoz�sa
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name='wsstepbystep')
CREATE DATABASE wsstepbystep
	COLLATE SQL_Latin1_General_CP1250_CI_AS;
GO

-- �j adatb�zis felhaszn�l� l�trehoz�sa
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name='wsstepbystepuser')
CREATE LOGIN wsstepbystepuser WITH PASSWORD='a', DEFAULT_DATABASE=wsstepbystep, CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF;
GO

-- Adatb�zis v�lt�s
USE wsstepbystep
GO

-- Tulajdonos be�ll�t�sa
EXEC dbo.sp_changedbowner @loginame='wsstepbystepuser', @map=false
GO


-- Tranzakci� be�ll�t�sa
ALTER DATABASE wsstepbystep SET ALLOW_SNAPSHOT_ISOLATION ON
GO
ALTER DATABASE wsstepbystep SET READ_COMMITTED_SNAPSHOT ON WITH ROLLBACK IMMEDIATE
GO

-- Adatb�zis v�lt�s
USE wsstepbystep
GO

SET NOCOUNT ON
GO
