import { defineApplicationConfig } from '@xueyi/vite-config';

export default defineApplicationConfig({
  overrides: {
    optimizeDeps: {
      include: [
        'echarts/core',
        'echarts/charts',
        'echarts/components',
        'echarts/renderers',
        'qrcode',
        '@iconify/iconify',
        'ant-design-vue/es/locale/zh_CN',
        'ant-design-vue/es/locale/en_US',
      ],
    },
    server: {
      proxy: {
        '/dev-api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          ws: true,
          rewrite: (path) => path.replace(new RegExp(`^/dev-api`), ''),
          // only https
          // secure: false
        },
      },
      warmup: {
        clientFiles: ['./index.html', './src/{views,components}/*'],
      },
    },
  },
});
