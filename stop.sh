
#!/bin/bash
TEMP_DIR="$(cd "$(dirname "$0")/.temp" && pwd)"
BACKEND_PID="$TEMP_DIR/backend.pid"
FRONTEND_PID="$TEMP_DIR/frontend.pid"
NGINX_PID="$TEMP_DIR/nginx.pid"

if [ -f /tmp/backend.pid ]; then
  kill "$(cat $BACKEND_PID)" && echo "Stopped backend"
  rm /tmp/backend.pid
else
  echo "No backend PID file found"
fi

if [ -f /tmp/frontend.pid ]; then
  kill "$(cat $FRONTEND_PID)" && echo "Stopped frontend"
  rm /tmp/frontend.pid
else
  echo "No frontend PID file found"
fi

if [ -f /tmp/nginx.pid ]; then
  kill "$(cat $NGINX_PID)" && echo "Stopped nginx"
  rm /tmp/nginx.pid
else
  echo "No nginx PID file found"
fi
