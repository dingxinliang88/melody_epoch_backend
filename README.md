<p align="center">
    <img src=./documents/assets/me.png width=138  alt="quick"/>
</p>



<h1 align='center'>Melody Epoch</h1>

<p align="center">
    <strong>
        音律纪元 —— 本项目旨在建立一个完整的乐队相关数据库系统，涵盖乐队、乐队成员、专辑、歌曲、演唱会、乐评和歌迷等信息。该数据库系统将提供一套完善的数据结构，以支持对乐队及其相关信息的高效管理和查询。
    </strong>
</p>

<p align="center">
  <a href="https://www.oracle.com/java/technologies/javase-jdk8-downloads.html"><img src="https://img.shields.io/badge/JDK-8+-orange.svg" alt="JDK Version"></a>
  <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Maven-3.8.3-blue.svg" alt="Maven Version"></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring%20Boot-2.7.x-green.svg" alt="Spring Boot Version"></a>
  <a href="http://www.apache.org/licenses/"><img alt="License" src="https://raster.shields.io/badge/license-Apache-yellow.svg"/></a>  
  <a href="https://codejuzi.icu/"><img src="https://img.shields.io/badge/Blog-codejuzi.icu-yellowgreen.svg" alt="My Blog"></a>
  <a href="https://github.com/dingxinliang88/"><img src="https://img.shields.io/badge/Author-youyi-blue.svg" alt="youyi"></a>
</p>

<h2 align='center'>🏁快速开始</h2>

### 基础开发环境

|  Env  | Version |
| :---: | :-----: |
|  JDK  |   8+    |
| Maven |  3.6.x  |
| MySQL | 5.7.x + |
| Redis |  5.x +  |

### 安装 && 启动

1. 克隆项目

  ```shell
  git clone https://github.com/dingxinliang88/melody_epoch_backend.git
  ```

2. 加载数据库文件`documents/db/init.sql`

3. 修改 `application-dev.yml`中的`spring.config.import`配置项

  配置文件模板`conf.properties`：

  ```properties
# MySQL
dev.mysql.username=your username
dev.mysql.password=your password
dev.mysql.url=jdbc:mysql://xxx.xxx.xxx.xxx:xxxx/database_name

# Redis 配置
spring.redis.host=xxx.xxx.xxx.xxx
spring.redis.port=xxxx
spring.redis.password=xxxxxx
spring.redis.database=x
# Redisson
melody.redis.host=xxx.xxx.xxx.xxx
melody.redis.port=xxxx
melody.redis.password=xxxxxx
melody.redis.database=x

# JWT 随机生成
jwt.secret_key=xxxx

# email 需要申请, smtp 服务
email.username=xxx@example.com
email.password=xxxxxx

# knife4j
knife4j.conf.base-package=io.github.dingxinliang88
knife4j.conf.title=Melody_Epoch API Documents
knife4j.conf.description=The project aims to establish a complete database system of bands, band members, albums, songs, concerts, music critics and fans. The database system will provide a complete set of data structures to support the efficient management and query of the band and its related information.
knife4j.conf.version=0.0.1
knife4j.conf.contact-name=codejuzi
knife4j.conf.contact-email=codejuzi@qq.com
knife4j.conf.contact-url=https://codejuzi.icu
knife4j.conf.license=Apache License 2.0
knife4j.conf.license-url=https://www.apache.org/licenses/LICENSE-2.0

# druid
melody.druid.username=admin
melody.druid.password=admin123
  ```

4. mvn 打包启动

  ```bash
  mvn clean install
  
  java -jar target/melody_epoch_backend.jar
  ```

5. 访问接口文档：在项目启动后，可以访问 http://localhost:8999/api/doc.html 查看API文档

6. 访问Druid控制台
    在项目启动后，可以访问 http://localhost:8999/api/druid ，账号密码为在配置文件中配置的账号密码，登录即可访问到Druid DashBoard

除此之外，本项目还提供了**Dockerfile**，你可以根据该文件构建出一份属于你自己的Docker镜像，构建之前，请修改其中的配置项，然后执行下列命令。

```shell
docker build -t your-docker-image-name .

docker run -p 8999:8999 your-docker-image-name
```

<h2 align='center'>👀项目亮点</h2>

1. DFA敏感词过滤
2. 多种设计模式融入使用，实现聚合搜索等功能
3. 多种数据结构结合使用，如位图、DFA等
4. AOP实现统一日志、鉴权、限流
5. 定时任务监督
6. 高度封装各种工具类，开箱即用
7. ......

<h2 align='center'>📌参与其中</h2>

欢迎各种形式的贡献！如果你有任何建议、bug 报告或想要为项目做出贡献，请按照以下步骤操作：

1. 在项目中提出 [Issue](https://github.com/dingxinliang88/melody_epoch_backend/issues)。
2. 如果你希望修复问题或增加新特性，请 [Fork](https://github.com/dingxinliang88/melody_epoch_backend/fork) 项目并提交 [Pull Request](https://github.com/dingxinliang88/melody_epoch_backend/pulls)。
3. 提交的 PR 需要符合 [Conventional Commits 规范](https://www.conventionalcommits.org/en/v1.0.0/)，以方便我们对项目进行版本控制。

感谢你的参与！

或者可以联系作者：[Youyi](codejuzi@163.com)

<h2 align='center'>©️Copyright</h2>

本项目完全基于Apache开源协议。

