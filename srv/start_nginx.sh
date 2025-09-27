#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/../scripts/config.sh"
source "$CONFIG_FILE"

mkdir -p "$LOG_DIR" "$TEMP_DIR"

sed "
s|{{ACCESS_LOG}}|$NGINX_ACCESS_LOG|g; 
s|{{ERROR_LOG}}|$NGINX_ERROR_LOG|g; 
s|{{PID}}|$NGINX_PID|g;" \
    "$NGINX_CONF_TEMPLATE" > "$NGINX_CONF"

echo -e "${YELLOW}Starting NGINX...${NC}"

nginx -c "$NGINX_CONF" > "$NGINX_LOG" 2>&1 &

echo $! > "$NGINX_PID"

timer=0
spinner=('\' '|' '/' '-')
i=0
echo "Waiting for NGINX to be ready..."
while true; do
    if curl -s "$NGINX_URL/nginx/status" | grep -q "up"; then
        printf "\r${GREEN}NGINX is ready! (in %ds)${NC}\n" "$timer"
        break
    else
        sleep 1
        ((timer++))
        i=$(( (i+1) % 4 ))
        printf "\rTimer: %d %s" "$timer" "${spinner[i]}"
    fi
done
