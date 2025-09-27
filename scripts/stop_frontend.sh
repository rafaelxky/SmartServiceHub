#!/usr/bin/env bash

souce ./config.sh

if [ -f $FRONTEND_PID ]; then
  kill "$(cat $FRONTEND_PID)" && echo -e "${GREEN} Stopped frontend ${NC}"
  rm $FRONTEND_PID
else
  echo -e "${RED} No frontend PID file found ${NC}"
fi
