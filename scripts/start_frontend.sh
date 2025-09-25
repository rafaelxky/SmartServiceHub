#!/usr/bin/env bash

source ./config.sh

cd $FRONTEND_DIR || exit
npm run dev 2>&1 | tee $FRONTEND_LOG

