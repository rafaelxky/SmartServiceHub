
#!/bin/bash

# -------------------------------
# Tor Hidden Service Setup Script
# -------------------------------

# Require sudo
if [ "$EUID" -ne 0 ]; then
  echo "This script must be run with sudo or as root."
  exit 1
fi

set -e  # Exit on any error

# Paths
HIDDEN_SERVICE_DIR="/var/lib/tor/my_hidden_service"
TORRC_FILE="/etc/tor/torrc"
FRONTEND_ENV="../SmartServiceHub/SmartServiceHub-frontend/src/frontend/.env.local"
HIDDEN_SERVICE_HOSTNAME="$HIDDEN_SERVICE_DIR/hostname"

# -------------------------------
# 1️⃣ Create hidden service directory
# -------------------------------
sudo mkdir -p "$HIDDEN_SERVICE_DIR"
sudo chown -R tor:tor "$HIDDEN_SERVICE_DIR"
sudo chmod 700 "$HIDDEN_SERVICE_DIR"
echo -e "\e[34mHidden service directory prepared: $HIDDEN_SERVICE_DIR\e[0m"

# -------------------------------
# 2️⃣ Append hidden service config to torrc if not already present
# -------------------------------
if ! grep -q "$HIDDEN_SERVICE_DIR" "$TORRC_FILE"; then
    sudo tee -a "$TORRC_FILE" > /dev/null <<EOL
HiddenServiceDir $HIDDEN_SERVICE_DIR/
HiddenServicePort 8080 127.0.0.1:8080
HiddenServicePort 5173 127.0.0.1:5173
EOL
    echo -e "\e[32mTor hidden service config added to $TORRC_FILE\e[0m"
else
    echo -e "\e[33mHidden service already configured in $TORRC_FILE\e[0m"
fi

# -------------------------------
# 3️⃣ Start or restart Tor
# -------------------------------
sudo systemctl restart tor
echo -e "\e[32mTor restarted.\e[0m"

# -------------------------------
# 4️⃣ Wait for hostname generation
# -------------------------------
echo -e "\e[33mWaiting for hostname generation...\e[0m"
for i in {1..10}; do
    if [ -f "$HIDDEN_SERVICE_HOSTNAME" ]; then
        break
    fi
    sleep 2
done

if [ ! -f "$HIDDEN_SERVICE_HOSTNAME" ]; then
    echo -e "\e[31mError: Hidden service hostname not found! Is Tor running?\e[0m"
    exit 1
fi

# -------------------------------
# 5️⃣ Export .onion URL to frontend .env.local
# -------------------------------
ONION_URL=$(sudo cat "$HIDDEN_SERVICE_HOSTNAME")
echo "VITE_ONION_URL=$ONION_URL" | sudo tee "$FRONTEND_ENV" > /dev/null
echo -e "\e[32mOnion URL exported to $FRONTEND_ENV: $ONION_URL\e[0m"

# -------------------------------
# 6️⃣ Check Tor status
# -------------------------------
sudo systemctl status tor --no-pager

echo -e "\e[32mSetup completed.\e[0m"
