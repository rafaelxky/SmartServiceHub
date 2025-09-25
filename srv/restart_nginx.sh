#!/usr/bin/env bash

source ../scripts/config.sh

sudo nginx -c $NGINX_CONF -s reload | tee $NGINX_LOG
echo "Nginx restarted!"
