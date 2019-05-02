# assistant
新华生活小助手的Android端代码

新华生活小助手项目:仿微博里面的树洞，是一款促进大学生交流的社交软件。                                                                                                            
本APP的软件主体是ViewPager+Fragment+TabLayout架构为基础的交流平台，用户能够自由发表、评论、预约。

本系统的Android端使用的框架技术如下：  
软件主体：ViewPager+Fragment+TabLayout架构  
请求数据：OkHttp  
解析Json：Gson  
列表：RecycleView和ListView  
加载图片：Glide  
圆形头像：CircleImageView  
图片压缩：Compressor  
下拉刷新：SmartRefreshLayout  

服务端代码：https://github.com/ningxiaojian31/spring-boot-assistant

### 1、该项目作为原生Android项目，开发工具是Android Studio

### 2、OkHtttp使用的是同步请求，需要开启线程，并且利用Handler用来传递消息更新UI

### 3、SharedPreferences本地存储登录信息，下次进入不用重新登录

### 4、二级评论使用了RecycleView嵌套RecycleView

### 5、相关的效果图请查看服务端代码的README


