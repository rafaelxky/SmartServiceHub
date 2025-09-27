#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/../scripts/config.sh"
source "$CONFIG_FILE"

sudo nginx -c $NGINX_CONF -s reload | tee $NGINX_LOG
echo "Nginx restarted!"
