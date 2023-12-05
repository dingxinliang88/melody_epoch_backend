CREATE DATABASE `db_design`;

USE `db_design`;

-- 用户表：用户ID、类别、账号、密码、手机号、邮箱
CREATE TABLE `user` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `type` TINYINT NOT NULL DEFAULT 0 COMMENT '0 - guest, 1 - admin , 2 - member, 3 - fan',
    `account` VARCHAR(32) NOT NULL COMMENT '账号',
    `password` VARCHAR(128) NOT NULL COMMENT '密码',
    `phone` VARCHAR(11) NULL COMMENT '电话',
    `email` VARCHAR(32) NULL COMMENT '邮箱',
    PRIMARY KEY (`user_id`)
) COMMENT '用户表';

-- 歌迷表：歌迷ID（用户ID）、姓名、性别、年龄、职业、学历
CREATE TABLE `fan` (
    `fan_id` INT NOT NULL,
    `name` VARCHAR(32) NOT NULL COMMENT '姓名',
    `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别 0 - 女， 1 - 男',
    `age` TINYINT NULL COMMENT '年龄',
    `career` VARCHAR(32) NULL COMMENT '职业',
    `education` VARCHAR(16) NULL COMMENT '学历',
    PRIMARY KEY (`fan_id`)
) COMMENT '歌迷表';

-- 乐队成员表：成员ID（用户ID）、姓名、性别、年龄、乐队分工、加入时间、离开时间、所属乐队ID、所属乐队名称、逻辑删除字段
CREATE TABLE `member` (
    `member_id` INT NOT NULL,
    `name` VARCHAR(32) NOT NULL COMMENT '姓名',
    `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '性别 0 - 女， 1 - 男',
    `age` TINYINT NULL COMMENT '年龄',
    `part` VARCHAR(32) NULL COMMENT '乐队分工',
    `join_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `leave_time` DATETIME NULL COMMENT '离开时间',
    `band_id` INT NULL COMMENT '所属乐队ID',
    `band_name` VARCHAR(32) NULL COMMENT '所属乐队名称',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
    PRIMARY KEY (`member_id`)
) COMMENT '乐队成员表';

-- 乐队表：乐队ID、乐队名称、成立时间、队长ID、乐队介绍、乐队人数、逻辑删除字段
CREATE TABLE `band` (
    `band_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL COMMENT '乐队名称',
    `found_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
    `leader_id` INT NULL COMMENT '队长ID',
    `profile` VARCHAR(255) NULL COMMENT '乐队介绍',
    `member_num` TINYINT NULL COMMENT '乐队人数',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
    PRIMARY KEY (`band_id`)
) COMMENT '乐队表';

-- 专辑表：专辑ID、专辑名称、发行公司、发表时间、乐队名称、专辑介绍、专辑平均分、逻辑删除字段
CREATE TABLE `album` (
    `album_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL COMMENT '专辑名称',
    `company` VARCHAR(32) NULL COMMENT '发行公司',
    `release_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
    `band_name` VARCHAR(32) NULL COMMENT '乐队名称',
    `profile` VARCHAR(255) NULL COMMENT '专辑介绍',
    `avg_score` FLOAT NULL COMMENT '专辑平均分',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
    PRIMARY KEY (`album_id`)
) COMMENT '专辑表';

-- 歌曲表：歌曲ID、歌曲名称、词曲作者、专辑ID、专辑名称、逻辑删除字段
CREATE TABLE `song` (
    `song_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL COMMENT '歌曲名称',
    `author` VARCHAR(32) NULL COMMENT '词曲作者',
    `album_id` INT NULL COMMENT '专辑ID',
    `album_name` VARCHAR(32) NULL COMMENT '专辑名称',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
    PRIMARY KEY (`song_id`)
) COMMENT '歌曲表';

-- 演唱会表：演唱会ID、演唱会名称、开始时间、结束时间、举办地点、举办乐队、上限人数
CREATE TABLE `concert` (
    `concert_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL COMMENT '演唱会名称',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `place` VARCHAR(32) NOT NULL COMMENT '举办地点',
    `band_name` VARCHAR(32) NOT NULL COMMENT '举办乐队',
    `max_num` INT NOT NULL COMMENT '上限人数',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
    PRIMARY KEY (`concert_id`)
) COMMENT '演唱会表';

-- 乐评表：乐评ID、专辑ID、父级乐评ID、评论内容、歌迷ID、评论时间、更新时间
CREATE TABLE `comment` (
    `comment_id` INT NOT NULL AUTO_INCREMENT,
    `album_id` INT NOT NULL COMMENT '专辑ID',
    `parent_id` INT NULL COMMENT '父级乐评ID',
    `content` VARCHAR(255) NOT NULL COMMENT '评论内容',
    `user_id` INT NOT NULL COMMENT '歌迷ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
    PRIMARY KEY (`comment_id`)
) COMMENT '乐评表';

-- 歌迷-喜欢-乐队：歌迷ID、乐队ID
CREATE TABLE `band_like` (
    `user_id` INT NOT NULL COMMENT '歌迷ID',
    `band_id` INT NOT NULL COMMENT '乐队ID',
    PRIMARY KEY (`user_id`, `band_id`)
) COMMENT '喜欢乐队表';

-- 歌迷-喜欢-歌曲：歌迷ID、歌曲ID
CREATE TABLE `song_like` (
    `user_id` INT NOT NULL COMMENT '听众ID',
    `song_id` INT NOT NULL COMMENT '歌曲ID',
    PRIMARY KEY (`user_id`, `song_id`)
) COMMENT '喜欢歌曲表';

-- 歌迷-喜欢-专辑：歌迷ID、专辑ID、打分
CREATE TABLE `album_like` (
    `user_id` INT NOT NULL COMMENT '听众ID',
    `album_id` INT NOT NULL COMMENT '专辑ID',
    `score` INT NOT NULL COMMENT '打分',
    PRIMARY KEY (`user_id`, `album_id`)
) COMMENT '喜欢专辑表';

-- 歌迷-参加-演唱会：歌迷ID、演唱会ID
CREATE TABLE `concert_join` (
    `user_id` INT NOT NULL COMMENT '听众ID',
    `concert_id` INT NOT NULL COMMENT '演唱会ID',
    PRIMARY KEY (`user_id`, `concert_id`)
) COMMENT '参加演唱会表';

-- 演唱会-歌曲：演唱会ID、乐队ID、歌曲ID Sorted List String
CREATE TABLE `concert_song` (
    `concert_id` INT NOT NULL COMMENT '演唱会ID',
    `band_id` INT NOT NULL COMMENT '乐队ID',
    `song_id_list` VARCHAR(512) NOT NULL COMMENT '歌曲ID List，逗号分割',
    PRIMARY KEY (`concert_id`, `band_id`)
) COMMENT '演唱会-乐队-歌曲表';

-- 最佳专辑排行表：专辑ID、专辑名称、创建时间
CREATE TABLE `album_rank` (
    `album_id` INT NOT NULL COMMENT '专辑ID',
    `album_name` VARCHAR(32) NOT NULL COMMENT '专辑名称',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`album_id`)
) COMMENT '最佳专辑排行表';