-- The data that is inject after the data.sql, hence why the id's are greater that
-- delete from sms_message where created_by in (10,11);
-- delete from application_user_role where user_id in (10,11);
--delete from application_user where user_id in (10,11);
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE user_bandwidth_credentials;
TRUNCATE TABLE sms_message;
TRUNCATE TABLE application_user_role;
TRUNCATE TABLE bandwidth_credentials;
TRUNCATE TABLE application_user;
SET REFERENTIAL_INTEGRITY TRUE;

insert into application_user (user_id,username, password, enabled)
values (10,'gary@email.com', 'gary', true);
insert into application_user_role (user_id, role_id)
values (10,1);
insert into application_user_role (user_id, role_id)
values (10,2);

insert into application_user (user_id,username, password, enabled)
values (11,'testy@email.com', 'password', true);
insert into application_user_role (user_id, role_id)
values (11,1);
insert into application_user_role (user_id, role_id)
values (11,2);

-- create bandwidth

insert into bandwidth_credentials (id,version, created_by, created_date, last_modified_by, last_modified_date,
message_api, message_secret,application_name ,application_id, sender_phone_number,dashboard_user_name, dashboard_pwd,
dashboard_account_id,dashboard_sub_account_id)
values (2,1, 10,now(), 10, now(),'832222a800d9cf9e1e8811fc527ee7aaea0f03a671265ce2a','fe227df3e6c6f1433247775147014fe3e91dab34d1a471a7','test-location-application','e5bca7db-123-40a3-8b74-35f846e8b549',217823123,
'username','password','23213123','123123');

insert into bandwidth_credentials (id,version, created_by, created_date, last_modified_by, last_modified_date,
message_api, message_secret,application_name ,application_id, sender_phone_number,dashboard_user_name, dashboard_pwd,
dashboard_account_id,dashboard_sub_account_id)
values (3,1, 10,now(), 10, now(),'message_api3','message_secret3','application_name3','application_id3',3333333,
'dashboard_user_name3','dashboard_pwd3','dashboard_account_id3','dashboard_sub_account_id3');

-- assign the bandwidth details to the users
insert into user_bandwidth_credentials (user_id,bandwidth_id) values(10,2);
insert into user_bandwidth_credentials (user_id,bandwidth_id) values(11,2);

-- gary test messages


insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (1,1, 10,now(), 10, now(), '', 'AAAAA Message', 1111111, 5012141397, DATEADD('DAY',5, NOW()), 'CREATED',2,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (2,1, 10,now(), 10, now(), '', 'BBBB Message', 1111111, 2222222, DATEADD('DAY',5, NOW()), 'CREATED',2,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (3,1, 10,now(), 10, now(), '', 'CCCC Message', 1111111, 2222222, DATEADD('DAY',5, NOW()), 'ERROR',2,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (4,1, 10,now(), 10, now(), '', 'DDDDD Message', 1111111, 2222222, DATEADD('DAY',2, NOW()), 'ERROR',2,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (5,1, 10,now(), 10, now(), '', 'EEEEEE Message', 1111111, 2222222, DATEADD('DAY',1, NOW()), 'ERROR',2,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (6,1, 10,now(), 10, now(), '', 'FFFFF Message', 1111111, 2222222, DATEADD('DAY',2, NOW()), 'ERROR',2,false);


insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (7,1, 11,now(), 11, now(), '', 'AAAAA Message', 1111111, 33333333, DATEADD('DAY',-5, NOW()), 'CREATED',3,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (8,1, 11,now(), 11, now(), '', 'BBBB Message', 1111111, 2222222, DATEADD('DAY',-7, NOW()), 'CREATED',3,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (9,1, 11,now(), 11, now(), '', 'CCCC Message', 1111111, 33333333, DATEADD('DAY',-10, NOW()), 'ERROR',3,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (10,1, 11,now(), 11, now(), '', 'DDDDD Message', 1111111, 33333333, DATEADD('DAY',-10, NOW()), 'ERROR',3,false);
insert into sms_message (id,version, created_by, created_date, last_modified_by, last_modified_date, deliver_response, message, receiver, sender, send_date, status,bandwidth_id,deleted)
values (11,1, 11,now(), 11, now(), '', 'EEEEEE Message', 1111111, 33333333, DATEADD('DAY',-8, NOW()), 'ERROR',3,false);