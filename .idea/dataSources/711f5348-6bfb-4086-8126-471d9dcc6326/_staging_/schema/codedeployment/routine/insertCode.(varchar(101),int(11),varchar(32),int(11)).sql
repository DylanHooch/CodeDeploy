CREATE PROCEDURE insertCode(IN CName_in VARCHAR(101), IN isBackup_in INT, IN MD5_in VARCHAR(32),
														IN ONO_in INT)
  BEGIN
	DECLARE tmpid INT;
	if LENGTH(CName_in)<100 THEN 
		select `CNO`  into tmpid from `codes` where `ONO` =ONO_in and `Cname` = CName_in;
		IF tmpid is NULL THEN 
			insert into `codes`(`CNO`,`Cname` , `IsBackup` , `MD5` , `ONO` )
				value(null,CName_in,isBackup_in,MD5_in,ONO_in);
		END IF;
	END IF;
END;
