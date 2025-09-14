
#!/bin/bash

if [ -f /tmp/backend.pid ]; then
  kill "$(cat /tmp/backend.pid)" && echo "Stopped backend"
  rm /tmp/backend.pid
else
  echo "No backend PID file found"
fi

if [ -f /tmp/frontend.pid ]; then
  kill "$(cat /tmp/frontend.pid)" && echo "Stopped frontend"
  rm /tmp/frontend.pid
else
  echo "No frontend PID file found"
fi

if [ -f /tmp/nginx.pid ]; then
  kill "$(cat /tmp/nginx.pid)" && echo "Stopped nginx"
  rm /tmp/nginx.pid
else
  echo "No nginx PID file found"
fi
