
#!/bin/bash

BASE_DIR="$(dirname "$0")/SmartServiceHub"

# Start backend and pipe output to backend.log
(
  cd "$BASE_DIR/SmartServiceHub-backend" || exit
  mvn spring-boot:run > backend.log 2>&1 &
)

# Start frontend and pipe output to frontend.log
(
  cd "$BASE_DIR/SmartServiceHub-frontend/src/frontend" || exit
  npm run dev > frontend.log 2>&1 &
)

echo "Started!"
