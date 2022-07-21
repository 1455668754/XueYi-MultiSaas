## 安装使用

- 获取项目代码

```bash
git clone https://gitee.com/xueyitiantang/MultiSaas.git
```

- 安装依赖

```bash
cd multi-ui

pnpm install
```

- 运行

```bash
vite
```

- 打包

```bash
cross-env NODE_ENV=production vite build && esno ./build/script/postBuild.ts
```
