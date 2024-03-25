-- 接口信息表
use api_db;

-- 创建用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';

-- 帖子表
create table if not exists post
(
    id            bigint auto_increment comment 'id' primary key,
    age           int comment '年龄',
    gender        tinyint  default 0                 not null comment '性别（0-男, 1-女）',
    education     varchar(512)                       null comment '学历',
    place         varchar(512)                       null comment '地点',
    job           varchar(512)                       null comment '职业',
    contact       varchar(512)                       null comment '联系方式',
    loveExp       varchar(512)                       null comment '感情经历',
    content       text                               null comment '内容（个人介绍）',
    photo         varchar(1024)                      null comment '照片地址',
    reviewStatus  int      default 0                 not null comment '状态（0-待审核, 1-通过, 2-拒绝）',
    reviewMessage varchar(512)                       null comment '审核信息',
    viewNum       int                                not null default 0 comment '浏览数',
    thumbNum      int                                not null default 0 comment '点赞数',
    userId        bigint                             not null comment '创建用户 id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '帖子';

-- 创建接口信息表
create table if not exists api_db.`interface_info`
(
    `id`             bigint                             not null auto_increment comment '主键' primary key,
    `name`           varchar(256)                       not null comment '接口名',
    `method`         varchar(256)                       not null comment '请求类型',
    `description`    varchar(512)                       null comment '描述',
    `status`         int      default 0                 not null comment '接口状态{0-关闭，1-开启）',
    'url'            varchar(512)                       not null comment '接口地址',
    'requestParams'  text                               not null comment '请求参数',
    `requestHeader`  text                               null comment '请求头',
    `update_time`    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `responseHeader` text                               null comment '响应头',
    `create_time`    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `is_deleted`     tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)',
    `userId`         varchar(256)                       not null comment '用户名'
) comment '接口信息表';


create table if not exists api_db.`user_interface_info`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `interfaceInfoId` bigint                             not null comment '接口id',
    `userId`          bigint                             not null comment '调用用户id',
    `totalNum`        int      default 0                 null comment '调用总次数',
    `leftNum`         int      default 0                 not null comment '剩余调用次数',
    `status`          int      default 0                 not null comment '0-正常，1-禁用',
    `update_time`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `create_time`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `is_deleted`      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系表';



insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('Sh6', 'ju', 0, '1Es', '87RbT', '苏浩');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('ZLS', 'Hy', 0, 'Gu', '7G', '史晓啸');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('K71J', 'rbzge', 0, 'THv5', 'WGExm', '钟懿轩');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('NTk', 'nV', 0, 'hH', 'S0r', '邹聪健');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('yzvc', 'Btc3W', 0, '8oMHf', 'pp', '钟鹤轩');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('zN', 'cy6', 0, 'tC7eK', 'Xz', '戴果');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('nsZ0C', 'G4', 0, 'lnqK', 'q0m', '赖健柏');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('hk2', 'O4q', 0, 'MjjpQ', 'Kv0v', '罗煜祺');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('maB7U', 'KzD', 0, 'lwenC', 'h35du', '侯钰轩');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('iV', 'lspJd', 0, 'zLZVS', 'vVugE', '高晓博');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('qyHKW', 'TPr', 0, '0y', 'aG', '郭烨伟');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('xZ', 'Jt', 0, '1NE', 'eK', '邹鹏煊');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('SqnZ0', '1rm', 0, 'V6y', '0b', '田鹏');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('yD', 'BIJmd', 0, 'lAy', 'vAkWT', '崔荣轩');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('jYrsD', 'yBo', 0, 'XItX', '4L', '洪彬');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('sWA', '6Y', 0, 'zjd', 'JAyi', '黄绍辉');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('s1S', 'pYZrS', 0, 'oCSn1', '9mWZ', '邵立诚');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('SX6', 'Qtrk', 0, 'jN', 'Rl', '任思淼');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('J7kv', 'UPL9', 0, 'N5y', 'Ex7X', '谢锦程');
insert into api_db.`interface_info` (`name`, `method`, `status`, `requestHeader`, `responseHeader`, `userId`)
values ('a6v', 'qlDP9', 0, '3u2zb', 'aRv', '钱修杰');