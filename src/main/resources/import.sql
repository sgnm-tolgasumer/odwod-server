/*CREATE TABLE WORK_TYPE(id int unsigned not null auto_increment primary key,
work_type varchar(100) not null);
CREATE TABLE DISTRICTS(id int unsigned not null auto_increment primary key,
district varchar(100) not null);
INSERT INTO DISTRICTS(district) VALUES ('Cankaya');*/

/*
  Pre-load of Districts and JobTypes Tables.
  */
INSERT INTO District(id , districtName) VALUES (100, 'Yenimahalle');
INSERT INTO District(id , districtName) VALUES (101, 'Kazan');
INSERT INTO District(id , districtName) VALUES (102, 'Cankaya');
INSERT INTO District(id , districtName) VALUES (103, 'Bilkent');


INSERT INTO WorkType(id , workType) VALUES (100, 'Paint');
INSERT INTO WorkType(id , workType) VALUES (101, 'Renovation');
INSERT INTO WorkType(id , workType) VALUES (102, 'Transportation');
INSERT INTO WorkType(id , workType) VALUES (103, 'Maintenance');
INSERT INTO WorkType(id , workType) VALUES (104, 'Cleaning');
INSERT INTO WorkType(id , workType) VALUES (105, 'Security');
INSERT INTO WorkType(id , workType) VALUES (106, 'Health');