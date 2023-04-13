# 基础镜像
FROM openjdk:17-oracle
# author
MAINTAINER xueyi

# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 挂载目录
VOLUME /home/xueyi
# 创建目录
RUN mkdir -p /home/xueyi
# 指定路径
WORKDIR /home/xueyi
# 复制jar文件到路径
COPY ./target/xueyi-modules-job.jar /home/xueyi/xueyi-modules-job.jar

# 启动定时任务服务
ENTRYPOINT ["java","-jar","xueyi-modules-job.jar"]