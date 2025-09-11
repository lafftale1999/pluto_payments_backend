create database pluto;

create table country(
id int not null auto_increment primary key,
name varchar(30),
phoneCode varchar(5),
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);

create table city(
id int not null auto_increment primary key,
name varchar(30),
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);

create table address(
id int not null auto_increment primary key,
name varchar(30) not null,
postalCode varchar(6) not null,
number int not null,
city int not null,
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
foreign key(city) references city(id)
);

create table card(
id int not null auto_increment primary key,
cardNum int not null,
pin int not null,
expiryDate timestamp default null,
issueDate timestamp default CURRENT_TIMESTAMP,
isActive bool not null default TRUE,
wrongEntries int not null default 0,
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);

create table user(
id int not null auto_increment primary key,
name varchar(30) not null,
lastname varchar(30) not null,
balance float not null,
maxLimit float not null,
points int not null,
phoneCode int not null,
phoneNum varchar(30) not null,
dob varchar(30) not null,
email varchar(30) not null,
userType ENUM('New','Regular','Loyal'),
card int not null,
address int not null,
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
foreign key(phoneCode) references country(phoneCode),
foreign key(card) references card(id),
foreign key(address) references address(id)
);

create table device(
id int not null auto_increment primary key,
isApproved bool not null default FALSE,
mac varchar(16) not null,
encryptKey varchar(8) not null,
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);

create table transaction(
id int not null auto_increment primary key,
cost float not null,
device int not null,
userId int not null,
created timestamp default CURRENT_TIMESTAMP,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
foreign key(userId) references user(id),
foreign key(device) references device(id)
);

create table invoice(
id int not null auto_increment primary key,
userId int not null,
cost float not null,
invStatus enum('PAID','PENDING','LATE'),
finalDate timestamp,
created timestamp default CURRENT_TIMESTAMP,
reminderDate timestamp,
lastUpdate timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
foreign key(userId) references user(id)
);

