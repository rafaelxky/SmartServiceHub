#!/bin/bash

cd ../SmartServiceHub

# P for pearl regex and o for just the match
ONION_ADDR=$(grep -Po '(?<=VITE_ONION_URL=).*' ./SmartServiceHub-frontend/src/frontend/.env.local)

echo "Backend ↓"
echo -e "\e[36mhttp://${ONION_ADDR}:8080\e[0m"
echo "Frontend ↓"
echo -e "\e[36mhttp://${ONION_ADDR}:8082\e[0m"
echo "Nginx ↓"
echo -e "\e[36mhttp://${ONION_ADDR}:8081\e[0m"

