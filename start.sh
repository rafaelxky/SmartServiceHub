
#!/bin/bash
# ================================================
# SmartServiceHub: Start Backend, Frontend & NGINX
# ================================================

echo "Started script"

# -------------------------------
# Config
# -------------------------------
BASE_DIR="$(cd "$(dirname "$0")/SmartServiceHub" && pwd)"
TEMP_DIR="$(cd "$(dirname "$0")/.temp" && pwd)"
MVN_URL="http://localhost:8080/status"
NGINX_CONF="$BASE_DIR/srv/nginx.conf"
LOG_DIR="$BASE_DIR/logs"

# Make sure log directory exists
mkdir -p "$LOG_DIR"

# PID files in project directory
BACKEND_PID="$TEMP_DIR/backend.pid"
FRONTEND_PID="$TEMP_DIR/frontend.pid"
NGINX_PID="$TEMP_DIR/nginx.pid"

# Remove old PID files
rm -f "$BACKEND_PID" "$FRONTEND_PID" "$NGINX_PID"

# Colors
GREEN="\e[32m"
YELLOW="\e[33m"
RED="\e[31m"
NC="\e[0m" # Reset color

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
# Start NGINX (non-root)
# -------------------------------
echo -e "${YELLOW}Starting NGINX...${NC}"
# Make sure nginx.conf uses a non-privileged port like 8081
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
        printf "${GREEN}Frontend URL: ${NC}http://localhost:5173/\n"
        printf "${GREEN}NGINX URL: ${NC}http://localhost:8081/\n"
        break
    else
        sleep 1
        ((timer++))
        i=$(( (i+1) % 4 ))  # rotate spinner
        printf "\rTimer: %d %s" "$timer" "${spinner[i]}"
    fi
done
