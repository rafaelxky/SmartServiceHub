#!/usr/bin/env bash

source ./config.sh

sed "
s|{{PID}}|$REDIS_PID|g;
s|{{LOGS}}|$REDIS_LOG|g;
s|{{REDIS_DIR}}|$REDIS_DIR|g;
" \
"$REDIS_CONFIG_TEMPLATE" > "$REDIS_CONFIG" 

redis-server $REDIS_CONFIG --daemonize yes

redis-cli ping
echo "Redis started. $REDIS_URL"
