
#!/bin/bash

BASE_DIR="$(dirname "$0")/SmartServiceHub"
MVN_URL="http://localhost:8080/status"

(

  if curl -s "$MVN_URL" | grep -q "up"; then
    echo "Backend is already running"
  else
      cd "$BASE_DIR/SmartServiceHub-backend" || exit
      mvn spring-boot:run > backend.log 2>&1 &
      echo $! > /tmp/backend.pid
  fi
)

(
    cd "$BASE_DIR/SmartServiceHub-frontend/src/frontend" || exit
    npm run dev > frontend.log 2>&1 &
    echo $! > /tmp/frontend.pid
)

timer=0
spinner=('\' '|' '/' '-')
i=0

echo "Starting..."
while true; do
  if curl -s "$MVN_URL" | grep -q "up"; then
    printf "\rThe app is ready to go! (in %ds)\n" "$timer"
    printf "Get the backend endpoints at http://localhost:8080/home"
    printf "Frontend URL http://localhost:5173/"

    break
  else
    sleep 1
    ((timer++))
    i=$(( (i+1) % 4 ))  # rotate spinner index
    printf "\rTimer: %d %s" "$timer" "${spinner[i]}"
  fi
done
