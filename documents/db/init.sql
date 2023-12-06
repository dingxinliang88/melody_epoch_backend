/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : db_design

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 06/12/2023 16:15:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
  `album_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '专辑名称',
  `company` varchar(32) DEFAULT NULL COMMENT '发行公司',
  `release_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  `band_name` varchar(32) DEFAULT NULL COMMENT '乐队名称',
  `profile` varchar(255) DEFAULT NULL COMMENT '专辑介绍',
  `avg_score` float DEFAULT NULL COMMENT '专辑平均分',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`album_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='专辑表';

-- ----------------------------
-- Table structure for album_like
-- ----------------------------
DROP TABLE IF EXISTS `album_like`;
CREATE TABLE `album_like` (
  `user_id` int NOT NULL COMMENT '听众ID',
  `album_id` int NOT NULL COMMENT '专辑ID',
  `score` int NOT NULL COMMENT '打分',
  PRIMARY KEY (`user_id`,`album_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='喜欢专辑表';

-- ----------------------------
-- Table structure for album_rank
-- ----------------------------
DROP TABLE IF EXISTS `album_rank`;
CREATE TABLE `album_rank` (
  `album_id` int NOT NULL COMMENT '专辑ID',
  `album_name` varchar(32) NOT NULL COMMENT '专辑名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`album_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='最佳专辑排行表';

-- ----------------------------
-- Table structure for band
-- ----------------------------
DROP TABLE IF EXISTS `band`;
CREATE TABLE `band` (
  `band_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '乐队名称',
  `found_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  `leader_id` int DEFAULT NULL COMMENT '队长ID',
  `profile` varchar(255) DEFAULT NULL COMMENT '乐队介绍',
  `member_num` tinyint DEFAULT NULL COMMENT '乐队人数',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`band_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='乐队表';

-- ----------------------------
-- Table structure for band_like
-- ----------------------------
DROP TABLE IF EXISTS `band_like`;
CREATE TABLE `band_like` (
  `user_id` int NOT NULL COMMENT '歌迷ID',
  `band_id` int NOT NULL COMMENT '乐队ID',
  PRIMARY KEY (`user_id`,`band_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='喜欢乐队表';

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `album_id` int NOT NULL COMMENT '专辑ID',
  `parent_id` int DEFAULT NULL COMMENT '父级乐评ID',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `user_id` int NOT NULL COMMENT '歌迷ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='乐评表';

-- ----------------------------
-- Table structure for concert
-- ----------------------------
DROP TABLE IF EXISTS `concert`;
CREATE TABLE `concert` (
  `concert_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '演唱会名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `place` varchar(32) NOT NULL COMMENT '举办地点',
  `band_name` varchar(32) NOT NULL COMMENT '举办乐队',
  `max_num` int NOT NULL COMMENT '上限人数',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`concert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='演唱会表';

-- ----------------------------
-- Table structure for concert_join
-- ----------------------------
DROP TABLE IF EXISTS `concert_join`;
CREATE TABLE `concert_join` (
  `user_id` int NOT NULL COMMENT '听众ID',
  `concert_id` int NOT NULL COMMENT '演唱会ID',
  PRIMARY KEY (`user_id`,`concert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参加演唱会表';

-- ----------------------------
-- Table structure for concert_song
-- ----------------------------
DROP TABLE IF EXISTS `concert_song`;
CREATE TABLE `concert_song` (
  `concert_id` int NOT NULL COMMENT '演唱会ID',
  `band_id` int NOT NULL COMMENT '乐队ID',
  `song_id_list` varchar(512) NOT NULL COMMENT '歌曲ID List，逗号分割',
  PRIMARY KEY (`concert_id`,`band_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='演唱会-乐队-歌曲表';

-- ----------------------------
-- Table structure for fan
-- ----------------------------
DROP TABLE IF EXISTS `fan`;
CREATE TABLE `fan` (
  `fan_id` int NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `gender` tinyint NOT NULL DEFAULT '0' COMMENT '性别 0 - 女， 1 - 男',
  `age` tinyint DEFAULT NULL COMMENT '年龄',
  `career` varchar(32) DEFAULT NULL COMMENT '职业',
  `education` varchar(16) DEFAULT NULL COMMENT '学历',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '0 - 未删除， 1 - 删除',
  PRIMARY KEY (`fan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='歌迷表';

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `member_id` int NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `gender` tinyint NOT NULL DEFAULT '0' COMMENT '性别 0 - 女， 1 - 男',
  `age` tinyint DEFAULT NULL COMMENT '年龄',
  `part` varchar(32) DEFAULT NULL COMMENT '乐队分工',
  `join_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `leave_time` datetime DEFAULT NULL COMMENT '离开时间',
  `band_id` int DEFAULT NULL COMMENT '所属乐队ID',
  `band_name` varchar(32) DEFAULT NULL COMMENT '所属乐队名称',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='乐队成员表';

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song` (
  `song_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '歌曲名称',
  `author` varchar(32) DEFAULT NULL COMMENT '词曲作者',
  `album_id` int DEFAULT NULL COMMENT '专辑ID',
  `album_name` varchar(32) DEFAULT NULL COMMENT '专辑名称',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='歌曲表';

-- ----------------------------
-- Table structure for song_like
-- ----------------------------
DROP TABLE IF EXISTS `song_like`;
CREATE TABLE `song_like` (
  `user_id` int NOT NULL COMMENT '听众ID',
  `song_id` int NOT NULL COMMENT '歌曲ID',
  PRIMARY KEY (`user_id`,`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='喜欢歌曲表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '0 - guest, 1 - admin , 2 - member, 3 - fan',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `account` varchar(32) DEFAULT NULL COMMENT '账号',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `salt` varchar(16) NOT NULL COMMENT '盐值',
  `phone` varchar(11) DEFAULT NULL COMMENT '电话',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '0 - 未删除， 1 - 删除',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;
