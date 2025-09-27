#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/config.sh"
source "$CONFIG_FILE"

if curl -s "$MVN_URL" | grep -q "up"; then
    echo -e "${GREEN}Backend is already running${NC}"
else
    echo -e "${YELLOW}Starting backend...${NC}"
    cd "$BACKEND_DIR" || exit
    mvn spring-boot:run > "$BACKEND_LOG" 2>&1 &
    echo $! > "$BACKEND_PID"
fi

timer=0
spinner=('\' '|' '/' '-')
i=0

echo "Waiting for backend to be ready..."
while true; do
    if curl -s "$MVN_URL" | grep -q "up"; then
        printf "\r${GREEN}The app is ready! (in %ds)${NC}\n" "$timer"
        printf "${GREEN}Backend endpoints: ${NC}http://localhost:8080/home\n"
        break
    else
        sleep 1
        ((timer++))
        i=$(( (i+1) % 4 ))
        printf "\rTimer: %d %s" "$timer" "${spinner[i]}"
    fi
done
