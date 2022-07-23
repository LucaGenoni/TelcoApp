drop TRIGGER if exists TR_tblBills_IntegrityInsert;
drop TRIGGER if exists TR_tblBills_UpdateUser;
drop TRIGGER if exists TR_tblBills_UpdateOrder;
drop TRIGGER if exists TR_tblBills_InsertAlert;

drop TRIGGER if exists TR_tblOrders_IntegrityInsert;
drop TRIGGER if exists TR_tblOrders_IntegrityUpdate;
drop TRIGGER if exists TR_tblOrders_UpdateUser;
drop TRIGGER if exists TR_tblOrders_InsertSchedule;

drop trigger if exists TR_tblPackages_Insertvw_Report134;
drop trigger if exists TR_tblOrders_Updatevw_Report134;

drop trigger if exists TR_tblPackageLastsPeriod_Insertvw_Report2;
drop trigger if exists TR_tblOrders_Updatevw_Report2;

drop trigger if exists TR_tblOptionals_Insertvw_Report6;
drop trigger if exists TR_tblOrders_Updatevw_Report6;
-- *************** *************** ***************
-- *************** *************** ***************
--                  Trigger Bill                 
-- *************** *************** ***************
-- *************** *************** ***************

DELIMITER $$
create trigger TR_tblBills_IntegrityInsert
before insert on tblBills for each row
begin 
	if 1 = (select isValid from tblOrders where PK_Orders = new.FK_Orders) then
		SIGNAL sqlstate '45001' set message_text = "Paid order do not accept any more bill";
	end if;
end;

DELIMITER $$
create trigger TR_tblBills_UpdateUser
after insert on tblBills for each row
begin
	declare var_Users int; 
	if new.result = 0 then
		select FK_Users into var_Users
			from tblOrders where PK_Orders = new.FK_Orders;  
		update tblUsers set isInsolvent = 1 where PK_Users = var_Users;
	end if;
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblBills_UpdateOrder
after insert on tblBills for each row
begin
	if new.result = 1 then
	    update tblOrders set isValid = new.result where PK_Orders = new.FK_Orders;
    end if;
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblBills_InsertAlert
after insert on tblBills for each row
begin
	declare var_Users int; 
	declare var_nRejected int;  
	declare var_username char(255);
	declare var_email char(255);
	declare var_amount double; 
        
	if new.result = 0 then
		select FK_Users into var_Users
			from tblOrders where PK_Orders = new.FK_Orders;  

        select count(*) into var_nRejected
			from tblBills b join tblOrders o on o.PK_Orders = b.FK_Orders
			where o.FK_Users = var_Users and o.isValid = 0;
		-- if it's bigger then 2 (3 or more) add alert     
        if var_nRejected >= 3 then  
		          
			select username, email into var_username, var_email
				from tblUsers where PK_Users = var_Users;
                  
			select sum(price) into var_amount
				from tblOrders 
                where FK_Users = var_Users and isValid = 0;
					
			insert into tblAlerts (FK_Users, username, email, amount, lastReject) 
				values (var_Users, var_username, var_email, var_amount , new.billDate);
		end if;
        
	end if;
end;
$$ DELIMITER ;

-- *************** *************** ***************
-- *************** *************** ***************
--                  Trigger Order                 
-- *************** *************** ***************
-- *************** *************** ***************

DELIMITER $$
create trigger TR_tblOrders_IntegrityInsert
before insert on tblOrders for each row
begin
	if new.isValid != 1 then
		SIGNAL sqlstate '45001' set message_text = "New order must be not valid";
	end if;
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblOrders_IntegrityUpdate
before update on tblOrders for each row
begin
	if old.isValid = 1 then
		SIGNAL sqlstate '45001' set message_text = "Paid order cannot be updated";
	end if;
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblOrders_UpdateUser
after update on tblOrders for each row
begin
	if new.isValid = 1 and 0 = (
		select count(*) from tblOrders o 
		where o.FK_Users = new.FK_Users and o.isValid = 0) then            
			update tblUsers set isInsolvent = 0 where PK_Users = new.FK_Users;
	end if;
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblOrders_InsertSchedule
after update on tblOrders for each row
begin
	declare var_deactiveDate datetime;
    
	if (old.isValid = 0 and new.isValid = 1) then
		set var_deactiveDate = DATE_ADD(new.startDate, interval (
			select months from tblPeriods p, tblOrders o 
			where o.PK_Orders = new.PK_Orders and p.PK_Periods = o.FK_Periods
			) month);  
   		insert into tblSchedules (activeDate, deactiveDate, FK_Orders, FK_Users) 
  			values (new.startDate, var_deactiveDate, new.PK_Orders, new.FK_Users);
	end if;
end;
$$ DELIMITER ;