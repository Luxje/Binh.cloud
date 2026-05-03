import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import mkcert from 'vite-plugin-mkcert'

export default defineConfig({
  plugins: [vue(), mkcert()],
  server: {
    https: true,
    port: 5173,
    proxy: {
      '/api': {
        target: 'https://localhost:8080', // Your Spring Boot port
        changeOrigin: true,
        secure: false, // Trust your self-signed certificate
      }
    }
  }
})