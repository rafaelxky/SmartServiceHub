#!/usr/bin/env bash

# ================================================
# SmartServiceHub: Start Backend, Frontend & NGINX
# ================================================


source ./scripts/config.sh

rm -f "$BACKEND_PID" "$FRONTEND_PID" "$NGINX_PID"

"$START_NGINX"
"$START_FRONTEND"
"$START_BACKEND"


