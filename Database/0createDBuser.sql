drop schema if exists `telco`;
create schema `telco`;

use telco;
create table tblUsers(
    PK_Users      int             auto_increment
    ,username     varchar(255)    not null
    ,password     varchar(255)    not null
    ,email        varchar(255)    not null
    ,role         int1            not null default 0
    ,isInsolvent  int1            not null default 0
    ,primary key(PK_Users)
    ,unique(username)
);

create table tblPackages(
    PK_Packages   int             auto_increment
    ,name         varchar(255)    not null
    ,primary key(PK_Packages)
    ,unique(name)
);

create table tblServices(
    PK_Services   int             auto_increment
    ,name         varchar(255)    not null
    ,type         varchar(40)     not null
    ,sms          int             null
    ,min          int             null
    ,gb           int             null
    ,feeSms       double          null
    ,feeMin       double          null
    ,feeGb        double          null
    ,primary key(PK_Services)
    ,check(feeSms>0 or feeSms=null)
    ,check(feeMin>0 or feeMin=null)
    ,check(feeGb>0 or feeGb=null)
    ,unique(name)
);

create table tblOptionals(
    PK_Optionals  int             auto_increment 
    ,name         varchar(255)    not null
    ,fee          double          not null default 0
    ,primary key(PK_Optionals)
    ,check (not(fee<0))
    ,unique(name)
);

create table tblPeriods(
    PK_Periods    int             auto_increment
    ,months       int             not null
    ,fee          double          not null default 0
    ,primary key(PK_Periods)
    ,check (not(fee<0))
    ,unique(months, fee)
);

create table tblPackageOffersService(
    FK_Packages    int             not null
    ,FK_Services   int             not null
    ,primary key(FK_Packages, FK_Services)
    ,foreign key(FK_Packages) references tblPackages(PK_Packages) on delete cascade
    ,foreign key(FK_Services) references tblServices(PK_Services) on delete cascade
);

create table tblPackageLastsPeriod(
    FK_Packages    int             not null
    ,FK_Periods    int             not null
    ,primary key(FK_Packages, FK_Periods)
    ,foreign key(FK_Packages) references tblPackages(PK_Packages) on delete cascade
    ,foreign key(FK_Periods) references tblPeriods(PK_Periods) on delete cascade
);

create table tblPackageIncludesOptional(
    FK_Packages    int             not null
    ,FK_Optionals  int             not null
    ,primary key(FK_Packages, FK_Optionals)
    ,foreign key(FK_Packages) references tblPackages(PK_Packages) on delete cascade
    ,foreign key(FK_Optionals) references tblOptionals(PK_Optionals) on delete cascade
);

create table tblOrders(
    PK_Orders      int             auto_increment
    ,startDate     datetime        not null
    ,price         double          not null
    ,creationDate  datetime        not null
    ,isValid       int1            default null
    ,FK_Users      int             not null
    ,FK_Packages   int             not null
    ,FK_Periods    int             not null
    ,primary key(PK_Orders)
    ,foreign key(FK_Users) references tblUsers(PK_Users) on delete cascade
    ,foreign key(FK_Packages) references tblPackages(PK_Packages) on delete cascade
    ,foreign key(FK_Periods) references tblPeriods(PK_Periods) on delete cascade
    ,unique(creationDate, FK_Users)
);

create table tblOrderIncludesOptional(
    FK_Orders      int             not null
    ,FK_Optionals  int             not null
    ,primary key(FK_Orders, FK_Optionals)
    ,foreign key(FK_Orders) references tblOrders(PK_Orders) on delete cascade
    ,foreign key(FK_Optionals) references tblOptionals(PK_Optionals) on delete cascade
);

create table tblSchedules(
    PK_Schedules   int             auto_increment
    ,activeDate    datetime        not null
    ,deactiveDate  datetime        not null
    ,FK_Orders     int             not null
    ,FK_Users      int             not null
    ,primary key(PK_Schedules)
    ,foreign key(FK_Users) references tblUsers(PK_Users) on delete cascade
    ,foreign key(FK_Orders) references tblOrders(PK_Orders) on delete cascade
    
);

create table tblBills(
    PK_Bills       int             auto_increment
    ,result        int1            not null
    ,billDate      datetime        not null
    ,FK_Orders     int             not null
    ,primary key(PK_Bills)
    ,foreign key(FK_Orders) references tblOrders(PK_Orders) on delete cascade
    ,unique(billDate, result)
);

create table tblAlerts(
    PK_Alerts      int             auto_increment
    ,FK_Users      int             not null
    ,username      varchar(255)    not null
    ,email         varchar(255)    not null
    ,amount        double          not null
    ,lastReject    datetime        not null
    ,primary key(PK_Alerts)
    ,foreign key(FK_Users) references tblUsers(PK_Users) on delete cascade
);

-- *************** *************** ***************
-- *************** *************** ***************
--                  Tables report                
-- *************** *************** ***************
-- *************** *************** ***************

create table vw_Report134(
    FK_Packages    int             not null
    ,nSold         int             default 0
    ,priceWithOpt  double          default 0
    ,priceNoOpt    double          default 0
    ,nAvgOpt       double          default 0
    ,primary key(FK_Packages)
    ,foreign key(FK_Packages) references tblPackages(PK_Packages) on delete cascade
);

create table vw_Report2(
    FK_Packages    int             not null
    ,FK_Periods    int             not null
    ,nSold         int             default 0
    ,primary key(FK_Packages, FK_Periods)
    ,foreign key(FK_Packages) references tblPackages(PK_Packages) on delete cascade
    ,foreign key(FK_Periods) references tblPeriods(PK_Periods) on delete cascade
);

create table vw_Report6(
    FK_Optionals   int             not null
    ,score         int             default 0
	,primary key(FK_Optionals)
    ,foreign key(FK_Optionals) references tblOptionals(PK_Optionals) on delete cascade
);

create table vw_Report6Best(
	FK_Optionals   int             not null
    ,score         int             default 0
	,primary key(FK_Optionals)
    ,foreign key(FK_Optionals) references tblOptionals(PK_Optionals) on delete cascade
);

-- ███████████████ ███████████████ ███████████████
-- ███████████████ ███████████████ ███████████████
--                     Trigger                
-- ███████████████ ███████████████ ███████████████
-- ███████████████ ███████████████ ███████████████

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

-- *************** *************** ***************
-- *************** *************** ***************
--                Trigger vw_Report134                
-- *************** *************** ***************
-- *************** *************** ***************

DELIMITER $$
create trigger TR_tblPackages_Insertvw_Report134
after insert on tblPackages for each row
begin
    insert into vw_Report134 (FK_Packages) values (new.PK_Packages);
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblOrders_Updatevw_Report134
after update on tblOrders for each row
begin
	declare var_nSold int;
	declare var_nOpt int;
	declare var_months int;
	declare var_fee double;
    
	if new.isValid = 1 then    
		select count(*) into var_nSold 
			from tblOrders 
            where FK_Packages = new.FK_Packages and isValid = 1;            
		select count(*) into var_nOpt
			from tblOptionals opt, tblOrderIncludesOptional oio 
			where oio.FK_Orders = new.PK_Orders and opt.PK_Optionals = oio.FK_Optionals;            
		select months, fee into var_months, var_fee
			from tblPeriods 
			where PK_Periods = new.FK_Periods;   

		update vw_Report134 set 
			nSold = var_nSold
			,priceWithOpt = priceWithOpt + new.price 
			,priceNoOpt = priceNoOpt + (var_fee * var_months)
			,nAvgOpt = ((nAvgOpt*(var_nSold-1)) + var_nOpt)/(var_nSold) 
			where FK_Packages = new.FK_Packages;
		
    end if;
end;
$$ DELIMITER ;

-- *************** *************** ***************
-- *************** *************** ***************
--                 Trigger vw_Report2                 
-- *************** *************** ***************
-- *************** *************** ***************

DELIMITER $$
create trigger TR_tblPackageLastsPeriod_Insertvw_Report2
after insert on tblPackageLastsPeriod for each row
begin
    insert into vw_Report2 (FK_Packages, FK_Periods) values (new.FK_Packages,new.FK_Periods);
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblOrders_Updatevw_Report2
after update on tblOrders for each row
begin	       
	if new.isValid = 1 then     
		update vw_Report2 set nSold = nSold + 1 
			where FK_Packages = new.FK_Packages and FK_Periods = new.FK_Periods;
    end if;
end;
$$ DELIMITER ;

-- *************** *************** ***************
-- *************** *************** ***************
--                 Trigger vw_Report6                 
-- *************** *************** ***************
-- *************** *************** ***************

DELIMITER $$
create trigger TR_tblOptionals_Insertvw_Report6
after insert on tblOptionals for each row
begin
    insert into vw_Report6 (FK_Optionals) values (new.PK_Optionals);
end;
$$ DELIMITER ;

DELIMITER $$
create trigger TR_tblOrders_Updatevw_Report6
after update on tblOrders for each row
begin	   
	declare var_months int;
    declare var_fee double;
    
	if new.isValid = 1 then	
		select months into var_months from tblPeriods where PK_Periods = new.FK_Periods;
		delete from vw_Report6Best where score = (select Max(r.score) from vw_Report6 r);
		update vw_Report6 r
				inner join tblOptionals o on r.FK_Optionals = o.PK_Optionals
				inner join tblOrderIncludesOptional oio on r.FK_Optionals = oio.FK_Optionals
			set r.score = r.score + (o.fee*var_months)
			where oio.FK_Orders = new.PK_Orders;
        insert into vw_Report6Best (FK_Optionals, score)
            select *
			from vw_Report6 
			where score = (select Max(r.score) from vw_Report6 r);
    end if;
end;
$$ DELIMITER ;

-- ███████████████ ███████████████ ███████████████
-- ███████████████ ███████████████ ███████████████
--                      Init                
-- ███████████████ ███████████████ ███████████████
-- ███████████████ ███████████████ ███████████████

-- tblServices
insert into tblServices (name, type) values 
	('base','Fixed Phone');
insert into tblServices (name, type, sms, min, feeSms, feeMin) values 
	('low mobile','Mobile Phone', '100', '100', '0.20', '0.15'),
	('high mobile','Mobile Phone', '1000', '1000', '0.29', '0.23');
insert into tblServices (name, type, gb, feeGb) values 
	('low home it','Fixed Internet', '100', '0.1'),
	('high home it','Fixed Internet', '1000', '0.3');
insert into tblServices (name, type, gb, feeGb) values 
	('low mobile it','Mobile Internet', '10', '1'),
	('high mobile it','Mobile Internet', '100', '2');

insert into tblPeriods (months, fee) values 
	('12', '20'),('12', '15'),
	('24', '18'),('24', '13'),
	('36', '15'),('36', '10');
           
insert into tblOptionals (name, fee) values 
	('SMS news feed', '5'),
    ('internet TV channel', '10'),
    ('call me back', '2');
    
insert into tblUsers (username, password, email, role) values 
	('a', 'a', 'a@a', 0),
	('s', 's', 's@s', 1);

insert into tblPackages (name) values 
	('Basic'), ('Family'), ('Business'), ('All Inclusive');
insert into tblPackageOffersService (FK_Packages,FK_Services) values 
	(1,1),
	(2,1), (2,4), (2,2),
	(3,1), (3,5), (3,2), (3,6),
	(4,1), (4,5), (4,3), (4,7);

insert into tblPackageLastsPeriod (FK_Packages,FK_Periods) values 
	(1,1),
    (2,4), (2,6),
    (3,1), (3,3),
    (4,3), (4,5);
    
insert into tblPackageIncludesOptional (FK_Packages,FK_Optionals) values 
    (1,3),
    (2,2),(2,3),
    (3,3),(2,1),
    (4,1),(4,2),(4,3);
    