CREATE PROCEDURE insertTestHost(IN address_to_insert VARCHAR(32), IN LID_in INT)
  BEGIN
	DECLARE tmpid INT;
	select testhost.tid into tmpid from `testhost`,`lt`
	WHERE  `lt`.`TID`=`testhost`.`TID` and `lt`.`LID` =LID_in
				 and `Address` = address_to_insert;
	IF tmpid is null THEN 
		insert into `testhost` value(null,address_to_insert);
		select `testhost`.`TID` into tmpid from `testhost`
		WHERE `Address` =address_to_insert;
		insert into `lt`(`LID` , `TID` ) value(LID_in,tmpid);
	END IF;
END;



