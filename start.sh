#!/usr/bin/env bash

# ================================================
# SmartServiceHub: Start Backend, Frontend & NGINX
# ================================================

echo "Started script"

# -------------------------------
# Config
# -------------------------------
CURRENT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$CURRENT_DIR/SmartServiceHub"
TEMP_DIR="$CURRENT_DIR/.temp"
NGINX_CONF="$CURRENT_DIR/srv/nginx.conf"
LOG_DIR="$CURRENT_DIR/logs"
MVN_URL="http://localhost:8080/status"

echo Base dir
echo $BASE_DIR
echo Temp dir
echo $TEMP_DIR
echo Nginx conf
echo $NGINX_CONF

mkdir -p "$LOG_DIR"

BACKEND_PID="$TEMP_DIR/backend.pid"
FRONTEND_PID="$TEMP_DIR/frontend.pid"
NGINX_PID="$TEMP_DIR/nginx.pid"

echo "Backend pid"
echo $BACKEND_PID
echo "Frotend pid"
echo $FRONTEND_PID
echo "Nginx pid"
echo $NGINX_PID

rm -f "$BACKEND_PID" "$FRONTEND_PID" "$NGINX_PID"

GREEN="\e[32m"
YELLOW="\e[33m"
RED="\e[31m"
NC="\e[0m"

# -------------------------------
# Start Backend
# -------------------------------

if curl -s "$MVN_URL" | grep -q "up"; then
    echo -e "${GREEN}Backend is already running${NC}"
else
    echo -e "${YELLOW}Starting backend...${NC}"
    cd "$BASE_DIR/SmartServiceHub-backend" || exit
    mvn spring-boot:run > "$LOG_DIR/backend.log" 2>&1 &
    echo $! > "$BACKEND_PID"
fi

# -------------------------------
# Start Frontend
# -------------------------------
echo -e "${YELLOW}Starting frontend...${NC}"
cd "$BASE_DIR/SmartServiceHub-frontend/src/frontend" || exit
npm run dev > "$LOG_DIR/frontend.log" 2>&1 &
echo $! > "$FRONTEND_PID"

# -------------------------------
# Start NGINX 
# -------------------------------
sed "
s|{{ACCESS_LOG}}|$LOG_DIR/nginx_access.log|g; 
s|{{ERROR_LOG}}|$LOG_DIR/nginx_error.log|g; 
s|{{PID}}|$TEMP_DIR/nginx.pid|g;" \
    "$CURRENT_DIR/srv/nginx.conf.template" > "$NGINX_CONF"

echo -e "${YELLOW}Starting NGINX...${NC}"
nginx -c "$NGINX_CONF" > "$LOG_DIR/nginx.log" 2>&1 &
echo $! > "$NGINX_PID"

# -------------------------------
# Wait for Backend to be Up
# -------------------------------
timer=0
spinner=('\' '|' '/' '-')
i=0

echo "Waiting for backend to be ready..."
while true; do
    if curl -s "$MVN_URL" | grep -q "up"; then
        printf "\r${GREEN}The app is ready! (in %ds)${NC}\n" "$timer"
        printf "${GREEN}Backend endpoints: ${NC}http://localhost:8080/home\n"
        printf "${GREEN}Frontend URL: ${NC}http://localhost:8082/\n"
        printf "${GREEN}NGINX URL: ${NC}http://localhost:8081/\n"
        break
    else
        sleep 1
        ((timer++))
        i=$(( (i+1) % 4 ))
        printf "\rTimer: %d %s" "$timer" "${spinner[i]}"
    fi
done
