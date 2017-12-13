CREATE PROCEDURE insertLocalHost(IN address_to_insert VARCHAR(32), OUT id INT)
  begin
	DECLARE tmpid int; 
	select `LID` into id from `localhost`  where `Address` = address_to_insert;
	if id is null then/*现在没有这个主机，可以插，否则无视请求*/
		insert into `localhost`(`LID`,`Address` ) value(null,address_to_insert);
		select `LID` into id from `localhost`  where `Address` = address_to_insert;
	end if;
	select id=id;
END;