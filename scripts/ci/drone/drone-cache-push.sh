#!/bin/sh

drone_cache_path="/drone/drone_cache/$DRONE_REPO"
drone_cache="$drone_cache_path/$1.cache.tar"

if [ "$1" = "ledger-frontend-build-artifact" ];
then
  echo "* $1"
  tar -cvf "$drone_cache" /drone/src/frontend/dist/
  sleep 2
  ls -l "$drone_cache_path"
else
  if [ "$1" = "ledger-backend-build-artifact" ];
  then
    echo "* $1"
    tar -cvf "$drone_cache" /drone/src/backend/build/
    sleep 2
    ls -l "$drone_cache_path"
  else
    echo "neither ledger-frontend-build-artifact or ledger-backend-build-artifact was passed as a parameter"
  fi
fi
