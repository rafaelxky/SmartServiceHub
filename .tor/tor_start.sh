#!/bin/bash

# Start Tor
sudo systemctl start tor

# Check if Tor started successfully
if systemctl is-active --quiet tor; then
    echo -e "\e[32mTor started successfully!\e[0m"
else
    echo -e "\e[31mFailed to start Tor. Check logs with 'sudo journalctl -u tor'.\e[0m"
    exit 1
fi
