#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/config.sh"
source "$CONFIG_FILE"

if [ -f $REDIS_PID ]; then
    kill "$(cat $REDIS_PID)" && echo -e "${GRENN} Stopped redis! ${NC}"
    rm $REDIS_PID
else
    echo -e "${RED} No redis PID file found ${NC}"
fi
