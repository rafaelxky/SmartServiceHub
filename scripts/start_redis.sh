#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/config.sh"
source "$CONFIG_FILE"

sed "
s|{{PID}}|$REDIS_PID|g;
s|{{LOGS}}|$REDIS_LOG|g;
s|{{REDIS_DIR}}|$REDIS_DIR|g;
" \
"$REDIS_CONFIG_TEMPLATE" > "$REDIS_CONFIG" 

redis-server $REDIS_CONFIG --daemonize yes

redis-cli ping
echo "Redis started. $REDIS_URL"
