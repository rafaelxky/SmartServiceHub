#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/config.sh"
source "$CONFIG_FILE"

if [ -f $FRONTEND_PID ]; then
  kill "$(cat $FRONTEND_PID)" && echo -e "${GREEN} Stopped frontend ${NC}"
  rm $FRONTEND_PID
else
  echo -e "${RED} No frontend PID file found ${NC}"
fi
