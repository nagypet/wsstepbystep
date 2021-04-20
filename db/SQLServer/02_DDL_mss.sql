-----------
-- MS SQL:
-----------

SET QUOTED_IDENTIFIER ON
GO

IF OBJECT_ID('DDL','P') IS NOT NULL DROP PROCEDURE DDL;
GO
CREATE PROCEDURE DDL 
    @sCmd VARCHAR(MAX),
    @sDB VARCHAR(10) = 'MSS'
WITH EXECUTE AS CALLER
AS
BEGIN TRY
	IF @sDB = 'MSS' AND (@sCmd IS NOT NULL OR RTRIM(@sCmd) <> '')
	BEGIN
		SELECT @sCmd = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(@sCmd, '--MSS!', ''), '/*MSS!', ''), '!MSS*/', ''), '--M!', ''), '/*M!', ''), '!M*/', '');
		EXECUTE(@sCmd);
	END;
END TRY
BEGIN CATCH
    DECLARE @ErrorMessage NVARCHAR(4000);
    DECLARE @ErrorSeverity INT;
    DECLARE @ErrorState INT;
	DECLARE @IsError INT;

    SELECT 
        @ErrorMessage = ERROR_MESSAGE(),
        @ErrorSeverity = ERROR_SEVERITY(),
        @ErrorState = ERROR_STATE();

	SELECT @IsError = CASE ERROR_NUMBER()
		WHEN 218 THEN NULL	-- drop type
		WHEN 1750 THEN NULL	-- Could not create constraint. See previous errors.
		WHEN 1913 THEN NULL	-- The operation failed because an index or statistics with name '...' already exists on table '...'.
		WHEN 2705 THEN NULL	-- Column names in each table must be unique. Column name '...' in table '...' is specified more than once.
		WHEN 2714 THEN NULL	-- There is already an object named '...' in the database.
		WHEN 3701 THEN NULL	-- drop table/index/view/default/synonym/procedure/function/trigger
		WHEN 3727 THEN NULL	-- drop constraint
		WHEN 4924 THEN NULL	-- drop column
		WHEN 2627 THEN NULL	-- Violation of PRIMARY KEY constraint
		ELSE 1
		END;
	
	IF @IsError IS NOT NULL 
	BEGIN
		PRINT 'This command caused an error:';
		PRINT @sCmd;
		RAISERROR (@ErrorMessage, @ErrorSeverity, @ErrorState);
	END;
END CATCH;
GO


IF OBJECT_ID('L','P') IS NOT NULL DROP PROCEDURE L;
GO
CREATE PROCEDURE L
    @sText VARCHAR(MAX),
    @sDB VARCHAR(10) = 'MSS'
WITH EXECUTE AS CALLER
AS
    IF @sDB IN ('MSS', 'M') AND @sText IS NOT NULL PRINT @sText;
GO
