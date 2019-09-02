# peacock
peacock，孔雀，一站式API在线开发平台，通过模块化思路，采用积木堆砌的方式进行在线API的组装开发，支持多机热发布、ApiDoc在线翻译、在线测试管理、服务治理，由JAVA语言

![avatar](https://github.com/hongframework/wiki-images/blob/master/peacock/0_overview.png?raw=true)

## peacock有哪些功能？


<p style="text-align:center"><img src='https://github.com/hongframework/wiki-images/blob/master/peacock/1_relation.png?raw=true' /></p>



* 在线开发节点（Manager）：负责权限管理，API在线开发、LARK集群节点管理，API发布管理
* API服务节点（Api）：获取授权API信息，在线提供API服务，记录调用Trace信息
* 文档节点（Doc）：获取授权API信息，API解析，在线提供API说明介绍
* 测试节点（Test）：获取授权API信息，在线提供API测试、以及测试用例保存、收藏、分享
* 监控节点（Trace）：获取API调用Trace，在线展示，分析接口调用情况

![avatar](https://github.com/hongframework/wiki-images/blob/master/peacock/2_lark_cluster.png?raw=true)

## hframework框架主要特点
* 使用系统抽取的组件进行在线配置开发，无需编写代码
* api的多组件之间调用关系，可发布为JVM内本地调用，也可以发布为微服务远程调用
* api的多组件之间，如不存在依赖关系可直接fork多线程并行调用，最终进行数据汇聚整合


## 文档
* 详见wiki
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%871.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%872.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%873.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%874.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%875.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%876.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%877.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%878.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%879.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8710.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8711.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8712.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8713.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8714.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8715.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8716.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8717.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8718.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8719.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8720.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8721.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8722.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8723.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8724.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8725.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8726.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8727.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8728.JPG)
![avatar](https://raw.githubusercontent.com/hongframework/wiki-images/master/hamster/peacock%E6%8E%A5%E5%8F%A3%E5%BC%80%E5%8F%91%E5%B9%B3%E5%8F%B0v0.1/%E5%B9%BB%E7%81%AF%E7%89%8729.JPG)

## 下载
* 待开放



## 联系我们
* zqhget1@163.com
