CREATE TABLE IF NOT EXISTS USERS (
  userid INT PRIMARY KEY auto_increment,
  username VARCHAR(20),
  salt VARCHAR,
  password VARCHAR,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY auto_increment,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userid INT,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileid INT PRIMARY KEY auto_increment,
    filename VARCHAR,
    contenttype VARCHAR,
    filesize VARCHAR,
    userid INT,
    filedata BLOB,
    foreign key (userid) references USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid INT PRIMARY KEY auto_increment,
    url VARCHAR(100),
    username VARCHAR (30),
    key VARCHAR,
    password VARCHAR,
    userid INT,
    foreign key (userid) references USERS(userid)
);

INSERT INTO USERS(username, salt, password, firstname, lastname) VALUES ('root', 'AAAAAAAAAAAAAAAAAAAAAA==', '6lvEAhfgZ0ElbqPiSX2okQ==', 'Lina', 'Walaa');

INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Saturday','GYM + Study');
INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Sunday','GYM + Study');
INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Monday','GYM + Study + Work');
INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Tuesday','GYM + Study + Work');
INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Wednesday','GYM + Study + Work');
INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Thursday','GYM + Study + Work');
INSERT INTO NOTES (userid, notetitle, notedescription) VALUES ('1', 'Friday','GYM + Study + Work');

INSERT INTO CREDENTIALS (userid, url, username, key, password) VALUES ('1', 'https://www.facebook.com', 'lalash', 'huLxb2mTjLj7ToFvLGLadw==', 'SefZrBi+LBG/Wa2AQK7jiw==');
INSERT INTO CREDENTIALS (userid, url, username, key, password) VALUES ('1', 'https://www.linkedin.com', 'boda', 'ur/cX6xCZcn0zhl0W+Vqsg==', 'ZQIRsb1s3r4hVvJlnjf5JA==');
INSERT INTO CREDENTIALS (userid, url, username, key, password) VALUES ('1', 'https://www.youtube.com', 'meshmesh', 'O+/3lnX6Xdc3LQFPt8HCmA==', 'VSgeyUAz+V2wEqE5nwkXnA==');