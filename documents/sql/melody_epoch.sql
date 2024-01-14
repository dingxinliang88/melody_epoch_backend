/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : melody_epoch

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 30/12/2023 22:43:20
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
  `release_time` datetime DEFAULT NULL COMMENT '发表时间，跟随发布标志一起',
  `band_name` varchar(32) DEFAULT NULL COMMENT '乐队名称',
  `song_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '专辑歌曲ID清单',
  `profile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专辑介绍',
  `avg_score` float NOT NULL DEFAULT '0' COMMENT '专辑平均分',
  `is_release` tinyint NOT NULL DEFAULT '0' COMMENT '是否已经发布， 0 - 未发布， 1 - 发布',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`album_id`),
  KEY `idx_band_name` (`band_name`),
  KEY `idx_avg_score` (`avg_score`,`name`,`band_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='专辑表';

-- ----------------------------
-- Table structure for album_like
-- ----------------------------
DROP TABLE IF EXISTS `album_like`;
CREATE TABLE `album_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` int NOT NULL COMMENT '听众ID',
  `album_id` int NOT NULL COMMENT '专辑ID',
  `score` float NOT NULL DEFAULT '0' COMMENT '打分',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `un_idx_album_user_id` (`album_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='喜欢专辑表';

-- ----------------------------
-- Table structure for band
-- ----------------------------
DROP TABLE IF EXISTS `band`;
CREATE TABLE `band` (
  `band_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '乐队名称',
  `found_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成立时间',
  `leader_id` int DEFAULT NULL COMMENT '队长ID',
  `profile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '乐队介绍',
  `member_num` tinyint DEFAULT NULL COMMENT '乐队人数',
  `is_release` tinyint NOT NULL DEFAULT '0' COMMENT '是否已经发布  0 - 未发布 ，1 - 发布',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`band_id`),
  UNIQUE KEY `uk_band_name` (`name`),
  KEY `idx_leader_id` (`leader_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='乐队表';

-- ----------------------------
-- Table structure for band_like
-- ----------------------------
DROP TABLE IF EXISTS `band_like`;
CREATE TABLE `band_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '歌迷ID',
  `band_id` int NOT NULL COMMENT '乐队ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `un_idx_user_band_id` (`user_id`,`band_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='喜欢乐队表';

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `album_id` int NOT NULL COMMENT '专辑ID',
  `parent_id` int NOT NULL DEFAULT '0' COMMENT '父级乐评ID',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `user_id` int NOT NULL COMMENT '歌迷ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`comment_id`),
  KEY `idx_album_id` (`album_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='乐评表';

-- ----------------------------
-- Table structure for concert
-- ----------------------------
DROP TABLE IF EXISTS `concert`;
CREATE TABLE `concert` (
  `concert_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '演唱会名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `place` varchar(32) NOT NULL COMMENT '举办地点',
  `band_id` int NOT NULL COMMENT '举办乐队ID',
  `band_name` varchar(32) NOT NULL COMMENT '举办乐队',
  `song_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '歌曲ID List，逗号分割',
  `max_num` int NOT NULL COMMENT '上限人数',
  `is_release` tinyint NOT NULL DEFAULT '0' COMMENT '是否已经发布， 0 - 未发布， 1 - 发布',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`concert_id`),
  KEY `idx_band_id` (`band_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='演唱会表';

-- ----------------------------
-- Table structure for concert_join
-- ----------------------------
DROP TABLE IF EXISTS `concert_join`;
CREATE TABLE `concert_join` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '听众ID',
  `concert_id` int NOT NULL COMMENT '演唱会ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_concert_id` (`concert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参加演唱会表';

-- ----------------------------
-- Table structure for fan
-- ----------------------------
DROP TABLE IF EXISTS `fan`;
CREATE TABLE `fan` (
  `fan_id` int NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `gender` tinyint NOT NULL DEFAULT '0' COMMENT '性别 0 - 女， 1 - 男',
  `age` tinyint NOT NULL DEFAULT '18' COMMENT '年龄',
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
  `age` tinyint NOT NULL DEFAULT '18' COMMENT '年龄',
  `part` varchar(32) DEFAULT NULL COMMENT '乐队分工',
  `join_time` datetime DEFAULT NULL COMMENT '加入时间',
  `leave_time` datetime DEFAULT NULL COMMENT '离开时间',
  `band_id` int DEFAULT NULL COMMENT '所属乐队ID',
  `band_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属乐队名称',
  `is_release` tinyint NOT NULL DEFAULT '0' COMMENT '所在乐队是否已经发布，0 - 未发布， 1 - 发布，未发布查询结果为NULL',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`member_id`),
  KEY `idx_band_id` (`band_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='乐队成员表';

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song` (
  `song_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '歌曲名称',
  `band_id` int NOT NULL COMMENT '乐队ID',
  `author` varchar(32) DEFAULT NULL COMMENT '词曲作者',
  `album_id` int DEFAULT NULL COMMENT '专辑ID',
  `album_name` varchar(32) DEFAULT NULL COMMENT '专辑名称',
  `is_release` tinyint NOT NULL DEFAULT '0' COMMENT '是否已经发布， 0 - 未发布， 1 - 发布',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除字段 0 - 未删除，1 - 删除',
  PRIMARY KEY (`song_id`),
  KEY `un_idx_band_album_id` (`band_id`,`album_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='歌曲表';

-- ----------------------------
-- Table structure for song_like
-- ----------------------------
DROP TABLE IF EXISTS `song_like`;
CREATE TABLE `song_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID\n',
  `user_id` int NOT NULL COMMENT '听众ID',
  `song_id` int NOT NULL COMMENT '歌曲ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `un_idx_user_song_id` (`user_id`,`song_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='喜欢歌曲表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称\n',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `salt` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '盐值',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '0 - guest, 1 - admin , 2 - member, 3 - fan',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '0 - 未删除， 1 - 删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_acc` (`account`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;
