#!/bin/sh

drone_cache="/drone/drone_cache/$1.cache.tar"


if [ "$1" = "ledger-frontend" ];
then
  echo "* $1"
  tar -cf "$drone_cache" /drone/src/frontend/node_modules/ /drone/src/frontend/dist/
else
  if [ "$1" = "ledger-backend" ];
  then
    tar -cf "$drone_cache" /drone/src/.gradle/ /drone/src/backend/build/
  else
    echo "neither ledger-frontend or ledger-backend was passed as a parameter"
  fi
fi
