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
COPY ./target/xueyi-modules-gen.jar /home/xueyi/xueyi-modules-gen.jar

# 启动代码生成服务
ENTRYPOINT ["java","-jar","xueyi-modules-gen.jar"]