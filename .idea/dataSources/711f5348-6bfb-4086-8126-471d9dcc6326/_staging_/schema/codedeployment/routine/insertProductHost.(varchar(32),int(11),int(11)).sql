CREATE PROCEDURE insertProductHost(IN address_to_insert VARCHAR(32),
																		IN GID_in INT, IN LID_in INT)
  BEGIN
	DECLARE tmpid INT;
	select `producthost`.pid into tmpid from `producthost`,`lp`
		WHERE  `lp`.`PID` =`producthost`.`PID` and `lp`.`LID` =LID_in
					 and `Address` = address_to_insert and `GID`=GID_in;
	IF tmpid is null THEN 
		insert into `producthost` value(null,address_to_insert,GID_in);
		select `producthost`.`pid` into tmpid from `producthost`
		WHERE `Address` =address_to_insert and `GID`=GID_in;
		insert into `lp`(`LID` , `PID` ) value(LID_in,tmpid);
	END IF;
END;
