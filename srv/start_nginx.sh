#!/usr/bin/env bash

CURRENT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$CURRENT_DIR/.."
LOG_DIR="$BASE_DIR/logs"
TEMP_DIR="$BASE_DIR/.temp"
NGINX_CONF="$CURRENT_DIR/nginx.conf"

mkdir -p "$LOG_DIR" "$TEMP_DIR"

# Colors
YELLOW='\033[1;33m'
GREEN='\033[1;32m'
NC='\033[0m'

sed "
s|{{ACCESS_LOG}}|$LOG_DIR/nginx_access.log|g; 
s|{{ERROR_LOG}}|$LOG_DIR/nginx_error.log|g; 
s|{{PID}}|$TEMP_DIR/nginx.pid|g;" \
    "$CURRENT_DIR/nginx.conf.template" > "$NGINX_CONF"

echo -e "${YELLOW}Starting NGINX...${NC}"
nginx -c "$NGINX_CONF" > "$LOG_DIR/nginx.log" 2>&1
echo -e "${GREEN}Started NGINX, visit http://localhost:8081 to get started${NC}"
