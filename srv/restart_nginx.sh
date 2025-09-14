#!/bin/bash

sudo nginx -c ./nginx.conf -s reload | tee nginx.log
