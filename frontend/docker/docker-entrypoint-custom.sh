#!/bin/sh

/usr/local/bin/envsubst < /usr/share/nginx/html/assets/runtimeEnvironment.template.js > /usr/share/nginx/html/assets/runtimeEnvironment.js
/docker-entrypoint.sh nginx -g 'daemon off;'
