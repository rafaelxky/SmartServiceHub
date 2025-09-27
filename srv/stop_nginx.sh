#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/../scripts/config.sh"
source "$CONFIG_FILE"

if [ -f $NGINX_PID ]; then
  kill "$(cat $NGINX_PID)" && echo -e "${GREEN} Stopped nginx ${NC}"
  rm $NGINX_PID
else
  echo -e "${RED} No nginx PID file found ${NC}"
fi
