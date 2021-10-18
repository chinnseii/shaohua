create database stzb ； USE stzb;
drop table if exists user;
create table if not exists user (
  id int primary key auto_increment comment 'id',
  email varchar (320) not null comment '邮箱',
  password varchar (255) not null comment '密码',
  lock_flg int comment '账号状态',
  login_time datetime comment '登陆时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);
drop table if exists userinfo;
create table if not exists userinfo(
  email varchar (320) not null primary key comment '邮箱',
  avatar_path varchar (255) comment '头像路径',
  signature varchar (255) not null comment '个性签名',
  message int (4) not null comment '消息',
  nick_name varchar (20) not null comment '游戏名',
  point int comment '个人积分',
  alliance_id int comment '盟id',
  alliance_name char (10) comment '盟名称',
  group_id int comment '团id',
  group_name char (10) comment '团名称',
  jurisdiction int comment '权限',
  create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);
drop table if exists groupmaster;
create table if not exists groupmaster(
  groupid int primary key auto_increment comment '团id',
  alliance_id int comment '盟id',
  group_name varchar (20) not null comment '团名称',
  group_introduce varchar (50) not null comment '团介绍',
  create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);
drop table if exists task;
CREATE TABLE if not exists task(
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '任务id',
  title varchar (50) NOT NULL COMMENT '任务名',
  content varchar (500) NOT NULL COMMENT '任务内容',
  own int NOT NULL COMMENT '任务发布者id',
  extent int NOT NULL COMMENT '盟级0团级1',
  Implementerid int NOT NULL COMMENT '任务对象',
  status int NOT NULL COMMENT '任务状态',
  x int COMMENT '任务地点X坐标',
  y int COMMENT '任务地点Y坐标',
  bonus int COMMENT '任务达成积分',
  start_time date comment '任务开始时间',
  end_time date comment '任务截至时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);
drop table if exists emailmaster;
create table if not exists emailmaster(
  id int primary key AUTO_INCREMENT comment '邮件id',
  title varchar (50) comment '邮件标题',
  content varchar (5000) not null comment '邮件内容',
  create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);
insert into emailmaster(title, content) value ('邮箱注册验证', '你的验证码是$1。有效时间5分钟');