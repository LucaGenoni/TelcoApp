
-- tblServices
insert into tblServices (type) values 
	('Fixed Phone');
insert into tblServices (type, sms, min, feeSms, feeMin) values 
	('Mobile Phone', '100', '100', '0.20', '0.15'),
	('Mobile Phone', '1000', '1000', '0.29', '0.23');
insert into tblServices (type, gb, feeGb) values 
	('Fixed Internet', '100', '0.1'),
	('Fixed Internet', '1000', '0.3');
insert into tblServices (type, gb, feeGb) values 
	('Mobile Internet', '10', '1'),
	('Mobile Internet', '100', '2');

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
    
