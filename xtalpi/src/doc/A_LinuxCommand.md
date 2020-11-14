# Linux 命令

1. netstat  : 网络管理
2. lsof     : 端口查看
3. ps       : 罗列进程
4. df       : 磁盘系统查看
5. top      : 查看内存使用详情
6. fdisk    : 查看磁盘情况
7. mount    : 将硬件挂载到某个目录下
8. ln       : 为文件创建软银链接
9. nmap     : 网络扫描和主机检测
10. lscpu   : 罗列cpu信息
11. ip      : ip 管理
    ip show route : 显示当前主机路由
    ip addr       : 显示网卡信息（物理网卡和虚拟网卡）

12. pkill -fi Postman

13. tail -f -n

14. curl
15. uwsgi -d -ini *.ini
16. uwsgi --reload *.pid
17. uwsgi --stop *.pid

18. netstat -antp | grep :port 查看端口由哪个服务开启

19. ipvsadm -Ln
20. nslookup
21. dig
22. iptables -t nat -nvL
23. telnet ip port
