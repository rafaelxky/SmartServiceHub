import { defineConfig, loadEnv } from 'vite'
import preact from '@preact/preset-vite'

export default defineConfig(({ mode }) => {
  // Load environment variables
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [preact()],
    server: {
      host: true,
      allowedHosts: [
        env.VITE_ONION_URL,
        'localhost',
        '127.0.0.1'
      ],
      port: 5173
    }
  }
})
