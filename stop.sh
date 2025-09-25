#!/usr/bin/env bash
source ./scripts/config.sh 

echo $TEMP_DIR

echo "Backend pid"
echo $BACKEND_PID
echo "Frotend pid"
echo $FRONTEND_PID
echo "Nginx pid"
echo $NGINX_PID

if [ -f $BACKEND_PID ]; then
  kill "$(cat $BACKEND_PID)" && echo -e "${GREEN} Stopped backend ${NC}"
  rm $BACKEND_PID
else
  echo -e "${RED} No backend PID file found ${NC}"
fi

if [ -f $FRONTEND_PID ]; then
  kill "$(cat $FRONTEND_PID)" && echo -e "${GREEN} Stopped frontend ${NC}"
  rm $FRONTEND_PID
else
  echo -e "${RED} No frontend PID file found ${NC}"
fi

if [ -f $NGINX_PID ]; then
  kill "$(cat $NGINX_PID)" && echo -e "${GREEN} Stopped nginx ${NC}"
  rm $NGINX_PID
else
  echo -e "${RED} No nginx PID file found ${NC}"
fi
