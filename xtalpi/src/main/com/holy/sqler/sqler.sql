-- 1- TOP N
-- 2- 有一个商场，每日人流量信息被记录在这三列信息中：序号 (id)、日期 (date)、 人流量 (people)。请编写一个查询语句，找出高峰期时段，要求连续三天及以上，并且每天人流量均不少于100

SET @users := "王天元, 吴炜坤, 翟珂, 郭宁, 张晨虹, 王泉, 李乐, 槐喆, 黄健, 董兵, 杨红, 雒文頔"
SELECT replace(@users, ', ', ',');
SELECT * FROM `user` u WHERE FIND_IN_SET(u.name, @users);
