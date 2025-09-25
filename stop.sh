#!/usr/bin/env bash
TEMP_DIR="$(cd "$(dirname "$0")/.temp" && pwd)"
BACKEND_PID="$TEMP_DIR/backend.pid"
FRONTEND_PID="$TEMP_DIR/frontend.pid"
NGINX_PID="$TEMP_DIR/nginx.pid"

echo $TEMP_DIR

echo "Backend pid"
echo $BACKEND_PID
echo "Frotend pid"
echo $FRONTEND_PID
echo "Nginx pid"
echo $NGINX_PID

if [ -f $BACKEND_PID ]; then
  kill "$(cat $BACKEND_PID)" && echo "Stopped backend"
  rm $BACKEND_PID
else
  echo "No backend PID file found"
fi

if [ -f $FRONTEND_PID ]; then
  kill "$(cat $FRONTEND_PID)" && echo "Stopped frontend"
  rm $FRONTEND_PID
else
  echo "No frontend PID file found"
fi

if [ -f $NGINX_PID ]; then
  kill "$(cat $NGINX_PID)" && echo "Stopped nginx"
  rm $NGINX_PID
else
  echo "No nginx PID file found"
fi
