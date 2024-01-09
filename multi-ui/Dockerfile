# 基础镜像
FROM nginx
# author
MAINTAINER xueyi

# 挂载目录
VOLUME /home/xueyi/projects/xueyi-ui
# 创建目录
RUN mkdir -p /home/xueyi/projects/xueyi-ui
# 指定路径
WORKDIR /home/xueyi/projects/xueyi-ui
# 复制conf文件到路径
COPY ./nginx/conf/nginx.conf /etc/nginx/nginx.conf
# 复制html文件到路径
COPY ./dist /home/xueyi/projects/xueyi-ui

# node 构建
FROM node:16-alpine as build-stage
# author
MAINTAINER xueyi
# 挂载目录
VOLUME /home/xueyi/projects/xueyi-ui
# 创建目录
RUN mkdir -p /home/xueyi/projects/xueyi-ui
# 指定路径
WORKDIR /home/xueyi/projects/xueyi-ui
COPY . ./
# 设置 node 阿里镜像
RUN npm config set registry https://registry.npmmirror.com
# 设置--max-old-space-size
ENV NODE_OPTIONS=--max-old-space-size=16384
# 设置阿里镜像、pnpm、依赖、编译
RUN npm install pnpm -g && \
    pnpm install --frozen-lockfile && \
    pnpm build:docker
# node部分结束
RUN echo "🎉 编 🎉 译 🎉 成 🎉 功 🎉"
# nginx 部署
FROM nginx:1.23.3-alpine as production-stage
# 复制html文件到路径
COPY --from=build-stage /app/dist /home/xueyi/projects/xueyi-ui
# 复制conf文件到路径
COPY --from=build-stage /app/nginx/conf/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
## 将/home/xueyi/projects/xueyi-ui/assets/index.js 和/home/xueyi/projects/xueyi-ui/_app.config.js中的"$vg_base_url"替换为环境变量中的VG_BASE_URL,$vg_sub_domain 替换成VG_SUB_DOMAIN，$vg_default_user替换成VG_DEFAULT_USER，$vg_default_password替换成VG_DEFAULT_PASSWORD 而后启动nginx
CMD sed -i "s|__vg_base_url|$VG_BASE_URL|g" /home/xueyi/projects/xueyi-ui/assets/entry/index-*.js /home/xueyi/projects/xueyi-ui/_app.config.js && \
    nginx -g 'daemon off;'
RUN echo "🎉 架 🎉 设 🎉 成 🎉 功 🎉"
