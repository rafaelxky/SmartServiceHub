#!/usr/bin/env bash

CURRENT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$CURRENT_DIR/.."
NGINX_CONF="$CURRENT_DIR/nginx.conf"
LOG_DIR="$BASE_DIR/logs"

sudo nginx -c $NGINX_CONF -s reload | tee $LOG_DIR/nginx.log

echo "Nginx restarted!"
