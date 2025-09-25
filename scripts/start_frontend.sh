#!/usr/bin/env bash

BASE_DIR="$(dirname "$0")/../SmartServiceHub/SmartServiceHub-frontend/src/frontend"
cd $BASE_DIR || exit
npm run dev 2>&1 | tee frontend.log

