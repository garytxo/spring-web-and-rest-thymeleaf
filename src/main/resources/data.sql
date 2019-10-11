INSERT INTO application_role VALUES (1, 'ROLE_ADMIN');
INSERT INTO application_role VALUES (2, 'ROLE_USER');

-- admin user

insert into application_user (user_id,username, password, enabled)
values (1,''admin@admin.com, 'admin', true);
insert into application_user_role (user_id, role_id)
values (1,1);
insert into application_user_role (user_id, role_id)
values (1,2);


insert into bandwidth_credentials (id,version, created_by, created_date, last_modified_by, last_modified_date,
message_api, message_secret,application_name ,application_id, sender_phone_number,dashboard_user_name, dashboard_pwd,
dashboard_account_id,dashboard_sub_account_id)
values (1,1, 1,now(), 1, now(),'meessageapi','messagesecret','test-location-application',
'e5bca7db-1158-40a3-8b74-sadsa',5012141397,
'userName','pwd','512312987','24122');


insert into user_bandwidth_credentials (user_id,bandwidth_id) values(1,1);




