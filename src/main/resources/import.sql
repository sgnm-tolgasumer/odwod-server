/*CREATE TABLE WORK_TYPE(id int unsigned not null auto_increment primary key,
work_type varchar(100) not null);
CREATE TABLE DISTRICTS(id int unsigned not null auto_increment primary key,
district varchar(100) not null);
INSERT INTO DISTRICTS(district) VALUES ('Cankaya');*/

/*
  Pre-load of Districts and JobTypes Tables.
  */
INSERT INTO District(id , districtName) VALUES (900, 'Yenimahalle');
INSERT INTO District(id , districtName) VALUES (901, 'Kazan');
INSERT INTO District(id , districtName) VALUES (902, 'Cankaya');
INSERT INTO District(id , districtName) VALUES (903, 'Bilkent');


INSERT INTO WorkType(id , workType) VALUES (900, 'Paint');
INSERT INTO WorkType(id , workType) VALUES (901, 'Renovation');
INSERT INTO WorkType(id , workType) VALUES (902, 'Transportation');
INSERT INTO WorkType(id , workType) VALUES (903, 'Maintenance');
INSERT INTO WorkType(id , workType) VALUES (904, 'Cleaning');
INSERT INTO WorkType(id , workType) VALUES (905, 'Security');
INSERT INTO WorkType(id , workType) VALUES (906, 'Health');