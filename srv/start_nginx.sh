#!/usr/bin/env bash

source ../scripts/config.sh

mkdir -p "$LOG_DIR" "$TEMP_DIR"

sed "
s|{{ACCESS_LOG}}|$NGINX_ACCESS_LOG|g; 
s|{{ERROR_LOG}}|$NGINX_ERROR_LOG|g; 
s|{{PID}}|$NGINX_PID|g;" \
    "$NGINX_CONF_TEMPLATE" > "$NGINX_CONF"

echo -e "${YELLOW}Starting NGINX...${NC}"
nginx -c "$NGINX_CONF" > "$NGINX_LOG" 2>&1
echo -e "${GREEN}Started NGINX, visit $NGINX_URL to get started${NC}"
