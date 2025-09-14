
#!/bin/bash

# Stop Tor
sudo systemctl stop tor

# Check if Tor actually stopped
if systemctl is-active --quiet tor; then
    echo -e "\e[31mFailed to stop Tor. It is still running.\e[0m"
    exit 1
else
    echo -e "\e[33mTor stopped successfully.\e[0m"
fi
