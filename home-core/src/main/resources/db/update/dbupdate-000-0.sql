create memory table T_AUTHENTICATION_TOKEN ( AUT_ID_C varchar(36) not null, AUT_IDUSER_C varchar(36) not null, AUT_LONGLASTED_B bit not null, AUT_CREATEDATE_D datetime not null, AUT_LASTCONNECTIONDATE_D datetime, primary key (AUT_ID_C) );
create memory table T_BASE_FUNCTION ( BAF_ID_C varchar(20) not null, primary key (BAF_ID_C) );
create memory table T_CONFIG ( CFG_ID_C varchar(50) not null, CFG_VALUE_C varchar(250) not null, primary key (CFG_ID_C) );
create memory table T_ROLE ( ROL_ID_C varchar(36) not null, ROL_NAME_C varchar(50) not null, ROL_CREATEDATE_D datetime not null, ROL_DELETEDATE_D datetime, primary key (ROL_ID_C) );
create memory table T_ROLE_BASE_FUNCTION ( RBF_ID_C varchar(36) not null, RBF_IDROLE_C varchar(36), RBF_IDBASEFUNCTION_C varchar(20) not null, RBF_CREATEDATE_D datetime not null, RBF_DELETEDATE_D datetime, primary key (RBF_ID_C) );
create memory table T_USER ( USE_ID_C varchar(36) not null, USE_IDROLE_C varchar(36) not null, USE_USERNAME_C varchar(50) not null, USE_PASSWORD_C varchar(60) not null, USE_EMAIL_C varchar(100) not null, USE_FIRSTCONNECTION_B bit default 0 not null, USE_CREATEDATE_D datetime not null, USE_DELETEDATE_D datetime, primary key (USE_ID_C) );
create memory table T_CAMERA ( CAM_ID_C varchar(36) not null, CAM_NAME_C varchar(100) not null, CAM_FOLDER_C varchar(4000) not null, CAM_CURRENT_C varchar(1000), CAM_CREATEDATE_D datetime not null, CAM_DELETEDATE_D datetime, primary key (CAM_ID_C) );
create memory table T_SENSOR ( SEN_ID_C varchar(36) not null, SEN_NAME_C varchar(100) not null, SEN_TYPE_C varchar(50) not null, SEN_CREATEDATE_D datetime not null, SEN_DELETEDATE_D datetime, primary key (SEN_ID_C) );
create cached table T_SENSOR_SAMPLE ( SES_ID_C varchar(36) not null, SES_IDSEN_C varchar(36) not null, SES_VALUE_N float not null, SES_CREATEDATE_D datetime not null, SES_TYPE_C varchar(30) not null, primary key (SES_ID_C) );
alter table T_AUTHENTICATION_TOKEN add constraint FK_AUT_IDUSER_C foreign key (AUT_IDUSER_C) references T_USER (USE_ID_C) on delete restrict on update restrict;
alter table T_ROLE_BASE_FUNCTION add constraint FK_RBF_IDBASEFUNCTION_C foreign key (RBF_IDBASEFUNCTION_C) references T_BASE_FUNCTION (BAF_ID_C) on delete restrict on update restrict;
alter table T_ROLE_BASE_FUNCTION add constraint FK_RBF_IDROLE_C foreign key (RBF_IDROLE_C) references T_ROLE (ROL_ID_C) on delete restrict on update restrict;
alter table T_USER add constraint FK_USE_IDROLE_C foreign key (USE_IDROLE_C) references T_ROLE (ROL_ID_C) on delete restrict on update restrict;
alter table T_SENSOR_SAMPLE add constraint FK_SES_IDSEN_C foreign key (SES_IDSEN_C) references T_SENSOR (SEN_ID_C) on delete restrict on update restrict;
create index if not exists IDX_SES_CREATEDATE_D on T_SENSOR_SAMPLE (SES_CREATEDATE_D);
create index if not exists IDX_SES_TYPE_C on T_SENSOR_SAMPLE (SES_TYPE_C);
insert into T_CONFIG(CFG_ID_C, CFG_VALUE_C) values('DB_VERSION', '0');
insert into T_BASE_FUNCTION(BAF_ID_C) values('ADMIN');
insert into T_BASE_FUNCTION(BAF_ID_C) values('PASSWORD');
insert into T_ROLE(ROL_ID_C, ROL_NAME_C, ROL_CREATEDATE_D) values('admin', 'Admin', NOW());
insert into T_ROLE_BASE_FUNCTION(RBF_ID_C, RBF_IDROLE_C, RBF_IDBASEFUNCTION_C, RBF_CREATEDATE_D) values('admin_ADMIN', 'admin', 'ADMIN', NOW());
insert into T_ROLE_BASE_FUNCTION(RBF_ID_C, RBF_IDROLE_C, RBF_IDBASEFUNCTION_C, RBF_CREATEDATE_D) values('admin_PASSWORD', 'admin', 'PASSWORD', NOW());
insert into T_ROLE(ROL_ID_C, ROL_NAME_C, ROL_CREATEDATE_D) values('user', 'User', NOW());
insert into T_ROLE_BASE_FUNCTION(RBF_ID_C, RBF_IDROLE_C, RBF_IDBASEFUNCTION_C, RBF_CREATEDATE_D) values('user_PASSWORD', 'user', 'PASSWORD', NOW());
insert into T_USER(USE_ID_C, USE_IDROLE_C, USE_USERNAME_C, USE_PASSWORD_C, USE_EMAIL_C, USE_FIRSTCONNECTION_B, USE_CREATEDATE_D) values('admin', 'admin', 'admin', '$2a$05$6Ny3TjrW3aVAL1or2SlcR.fhuDgPKp5jp.P9fBXwVNePgeLqb4i3C', 'admin@localhost', true, NOW());
insert into T_SENSOR(SEN_ID_C, SEN_NAME_C, SEN_CREATEDATE_D, SEN_TYPE_C) values('main-elec', 'Main electricity meter', NOW(), 'ELECTRICITY');
insert into T_SENSOR(SEN_ID_C, SEN_NAME_C, SEN_CREATEDATE_D, SEN_TYPE_C) values('main-temp', 'Main temperature sensor', NOW(), 'TEMPERATURE');
insert into T_SENSOR(SEN_ID_C, SEN_NAME_C, SEN_CREATEDATE_D, SEN_TYPE_C) values('main-humidity', 'Main humidity sensor', NOW(), 'HUMIDITY');
insert into T_SENSOR(SEN_ID_C, SEN_NAME_C, SEN_CREATEDATE_D, SEN_TYPE_C) values('main-movement', 'Main movement sensor', NOW(), 'MOVEMENT');
