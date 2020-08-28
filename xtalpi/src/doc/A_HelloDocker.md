#### 认识Docker

---
容器就是一个视图隔离、资源可限制、独立文件系统的进程集合。  
所谓“视图隔离”就是能够看到部分进程以及具有独立的主机名等；  
控制资源使用率则是可以对于内存大小以及 CPU 使用个数等进行限制。  
容器就是一个进程集合，它将系统的其他资源隔离开来，具有自己独立的资源视图。    
容器具有一个独立的文件系统，因为使用的是系统的资源，所以在独立的文件系统内不需要具备内核相关的代码或者工具，  
我们只需要提供容器所需的二进制文件、配置文件以及依赖即可。  
只要容器运行时所需的文件集合都能够具备，那么这个容器就能够运行起来。  

---
### Docker-compose
一个 project 当中可包含多个service，每个service中定义了container运行的image，args，dependence,   
一个 service 当中可包括多个container instance。  

Infrastructure as a service: 基础设施级服务  
platform as a service      : 平台服务  
　　　　需要运维构建环境，后边有了自动化运维工具，ansible puppet  
　　　　docker成为paas 的下一代标准  
　　　　主机和容器，容器与容器之间的网络通信：通过DNAT通信，需要经过防火墙，效率低  
　　　　容器集群化的管理迫在眉睫
software as a service      : 软件服务

参照 i18n internationalization　k8s kubernetes 

k8s： 
 
    google　10 年的容器化基础架构　造就了容器编排系统（资源管理系统）borg 系统，它是对容器化编排的实现。  
    轻量开源，使用　编译性语言go实现, 该语言在语言级别就支持进程管理，资源管理过程中消耗的资源少  
    资源管理可以是平滑的弹性伸缩，内部已实现模块之间的负载均衡，采用了ipvs 实现负载均衡。  
    lvs + keepalive: ？？？？  
    服务发现，暴露负载均衡的pod 给　客户端，采用　轮训算法（ＲＲ）实现，和　keepalive 一样。
    
mesos         
yarn  
docker-swarm  主要对容器化实现的支持, 但也可支持容器大集群化管理。






