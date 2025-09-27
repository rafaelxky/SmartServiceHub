#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/config.sh"
source "$CONFIG_FILE"

if [ -f $BACKEND_PID ]; then
  kill "$(cat $BACKEND_PID)" && echo -e "${GREEN} Stopped backend ${NC}"
  rm $BACKEND_PID
else
  echo -e "${RED} No backend PID file found ${NC}"
fi
