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

Infrastructure as a service: 基础设施服务  
platform as a service      : 平台服务  
software as a service      : 软件服务
  
k8s：  
    borg 系统         
    对容器化编排的实现  
mesos         
yarn  
docker-swarm  对容器化实现的支持






