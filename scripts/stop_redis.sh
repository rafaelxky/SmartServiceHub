#!/usr/bin/env bash

source ./config.sh

if [ -f $REDIS_PID ]; then
    kill "$(cat $REDIS_PID)" && echo -e "${GRENN} Stopped redis! ${NC}"
    rm $REDIS_PID
else
    echo -e "${RED} No redis PID file found ${NC}"
fi
