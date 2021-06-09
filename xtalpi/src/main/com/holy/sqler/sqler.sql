
use `marvin-prod-20210426`;
SELECT pur.id, u.id, u.name, p.id, p.name from project_user_role pur
	inner join user_role ur on pur.user_role_id =ur.id
	inner join `user` u on ur.user_id = u.id
	inner join project p on pur.project_id = p.id
	WHERE pur.member_type =2;

SELECT * from project_stage ps1 join (SELECT version_id FROM project_stage where latest = 0) ps2 on ps1.version_id = ps2.version_id where ps1.latest = 1;
SELECT * FROM project_stage where latest = 0;

use `marvin-prod-20201028`;


DELETE from message_receive where receive_time >= "2020-11-23 10:30:00.0"

SELECT pj.name, wps.weekly_id, wps.percent, wps.user_id from (SELECT * from work_percent wp where wp.project_id != 5 and wp.weekly_id >= 40 and wp.user_id in (31, 96,102)) wps join project pj on pj.id = wps.project_id;

use `marvin-prod-20210119`

SELECT DISTINCT wp.project_id from work_percent wp where wp.project_id not in (1,2,3,4,5, 40, 129) and version_id  is NULL;

SELECT m.source_id, m.user_id, m.department_id from matter m where m.jump_to='percent' and m.done_time > (DATE_ADD(m.close_time, )) ORDER BY m.source_id;

SET SESSION `now_date` = now();

SELECT purs.project_id, DISTINCT u.name from (SELECT * from project_user_role pur where pur.role_id = 5 and ) purs JOIN `user` u on u.id = purs.user_id;

SELECT ppm.`number`, ppm.`pname`, ppm.state, u.`name` as uname, ppm.`mname`, ppm.`start`, ppm.`end` FROM project_user_role pur, `user` u,
	(SELECT pp.`id` as pid, pp.`number`, pp.`name` as pname, pp.state, m.`id` as `mid`, m.`name` as mname, m.`start`, m.`end` from
		(SELECT p.id, p.`number`, p.name, p.state from project p where p.project_type_id = 5 and p.state >= 0 and p.state != 5 and p.state != 4) pp
			LEFT JOIN milestone m on pp.id = m.project_id) ppm where ppm.pid = pur.project_id and pur.role_id = 5 and pur.user_id = u.id
		GROUP BY ppm.`number`, ppm.`pname`, ppm.state, u.`name`, ppm.`mid` ORDER BY ppm.`number`, ppm.`start`;

SELECT m.name, m.start, m.`end`, m.project_id from milestone m where m.`start` <= now() and m.`end` >=now();

and m.project_id in (SELECT p.id from project p where p.project_type_id = 5);

SELECT a.start, COUNT(1) as `count` from approval a where a.`start` != 1 GROUP BY a.`start` ORDER BY `count`;

SELECT pj.`number`, pj.`name`, ps.`state`, ps.`start` as `stage_start`, ps.`end` as `stage_end`,
	   cp.`start`  as `source_start`, cp.`end` as `source_end`, cp.`cpu`, cp.`gpu`, cp.`memory`
from compute_preset cp join project pj on cp.project_id = pj.id join project_stage ps on cp.stage_id = ps.id ORDER BY cp.`start`;

SELECT m.end, count(*) from milestone m group by m.`end` ORDER by count(*) ;

-- weekly_id = 66 and project_id not in (1, 2, 3, 4, 5, 40, 129, 201, 242)

SELECT * from work_percent wp cross join percent_preset pp on wp.version_id = pp.version_id WHERE wp.user_id = pp.user_id and wp.weekly_id = 66 and pp.mon_date = "2021-03-29" and pp.sun_date = "2021-04-04";

-- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
-- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
-- <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

use `marvin-prod-20210531`;
SET @users := "王天元,吴炜坤,翟珂,郭宁,张晨虹,王泉,李乐,槐喆,黄健,董兵,杨红,雒文頔";

SET @users2 =  '"王天元", "吴炜坤", "翟珂", "郭宁", "张晨虹", "王泉", "李乐", "槐喆", "黄健", "董兵", "杨红", "雒文頔"';

-- invalid operate
-- set @ users3 = SELECT replace(@users2, '", "', ',');

SELECT * from department;
SELECT * from `user` where department_id = 11;
SELECT * FROM `user` u WHERE FIND_IN_SET(u.name, @users);

CREATE TEMPORARY TABLE `usersid` AS(SELECT id from `user` where FIND_IN_SET(name, @users));
SELECT * FROM usersid;
SELECT * from `user` u where u.id in (SELECT * FROM usersid);
SELECT * from `user` u, (SELECT * FROM usersid) u2 where u.id = u2.id;
SELECT * from `user` u join (SELECT * FROM usersid) u2 on u.id = u2.id;

-- UPDATE `user` set department_id=11 WHERE FIND_IN_SET(name, @users);
-- UPDATE matter set department_id=11 where user_id in (SELECT id from `user` where FIND_IN_SET(name, @users));
-- UPDATE okr set department_id=11 where user_id in (SELECT id from `user` where FIND_IN_SET(name, @users));

-- 查询项目中版本号最大的记录（主键）由于最大记录不具有唯一性所以无法直接给出记录
-- PS:在多表之间非连接查询时，第一个表如果带 where，则只能在外套一层
SELECT * from project_version pv,
	(SELECT max(`version`) as `max_version` FROM `project_version` where project_id = 11) s WHERE pv.project_id = 11 and pv.version = s.`max_version`;

-- 查询每个项目下版本号居于前二并按顺序排序的记录
# SELECT * FROM project_version pv group by project_id;  -- sql_mode=only_full_group_by

# 查询一个班级的前两名
SELECT * from project_version pv where pv.project_id = 11 ORDER by pv.version limit 2;

-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# 查询各科成绩的 Top 1 （包含并列）
-- 自连查询
SELECT * from project_version pv2,
	(SELECT pv.project_id, max(pv.version) as max_version FROM project_version pv group by pv.project_id) pv3
	where pv2.project_id = pv3.project_id and pv3.max_version=pv2.version;

-- 相关子查询： 可以理解为2层循环，要想执行内层的查询，需要先从外层查询到1个值出来。
--            执行的顺序是，父查询1个值，子查询对这个得到的值进行1轮查询，总查询次数是m*n
--            因为子查询需要父查询的结果才能执行，所以叫相关子查询
-- 每一条记录都过，如果满足最大版本号相等则保留该条记录
SELECT * from project_version pv WHERE pv.version = (SELECT max(version) from project_version pv2 where pv.project_id = pv2.project_id);
SELECT * from project_version pv WHERE (SELECT COUNT(*) from project_version pv2 where pv.project_id = pv2.project_id and pv.version < pv2.version) < 1;
SELECT * from project_version pv WHERE NOT EXISTS (SELECT 1 from project_version pv2 where pv.project_id = pv2.project_id and pv.version < pv2.version);
-- 非相关子查询：子查询的不需要父查询把结果传进来，所以叫不相关子查询
--           执行顺序是子查询先执行，得到结果后传给父查询，父查询就不用每次查询完1个值后再执行1轮子查询
--           由于2个查询是分开的，无关联的，所以叫不相关子查询，查询次数是m+n
-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# 查询各科成绩的 Top N
-- 目标集较小，单独查出之后，排序取top
-- SELECT * from (
(SELECT * from project_version pv where pv.project_id = 11 ORDER by pv.version limit 2)
UNION ALL
(SELECT * from project_version pv where pv.project_id = 12 ORDER by pv.version limit 2)
UNION ALL
(SELECT * from project_version pv where pv.project_id = 9 ORDER by pv.version limit 2)
-- ) ORDER BY project_id; -- Every derived table must have its own alias

-- 自身左连接
SELECT pv.project_id, pv.version, count(*) as ct from project_version pv left join project_version pv2 on pv.project_id = pv2.project_id
where pv.version < pv2.version GROUP BY pv.project_id, pv.version HAVING ct <= 2 ORDER BY pv.project_id, pv.version;

-- 自关联子查询：判断每一条记录，比a中当前记录大的条数是否为2，如果有2条比较大，则符合。筛选出全部记录，最后按照课程和学分排序。但是子查询进行了count(1)次计算，性能很差。
--            空间和时间的较量

-- 半连接+count+having


-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 表自连查询可解决的问题有哪些 ？？？？
-- 窗口函数 widget
-- 不知道你为啥不知道
