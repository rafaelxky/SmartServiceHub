import { defineConfig } from 'vite'
import preact from '@preact/preset-vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [preact()],
  server: {
    host: true, // listen on all interfaces, not just localhost
    allowedHosts: [
      'phcsskv6qsvplkt53ubqcpvbpotecvqewn7vheqizzvoh4iu7zxrznyd.onion',
      'localhost',
      '127.0.0.1'
    ],
    port: 5173
  }
})
