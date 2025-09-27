#!/usr/bin/env bash

# ================================================
# SmartServiceHub: Start Backend, Frontend & NGINX
# ================================================

echo "Started script"

source ./scripts/config.sh

rm -f "$BACKEND_PID" "$FRONTEND_PID" "$NGINX_PID"

echo "nginx"
"$START_NGINX"
echo "frontend"
"$START_FRONTEND"
echo "backend"
"$START_BACKEND"


