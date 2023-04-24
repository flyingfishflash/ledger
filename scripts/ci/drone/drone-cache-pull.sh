#!/bin/sh

drone_cache_path="/drone/drone_cache/$DRONE_REPO"
drone_cache="$drone_cache_path/$1.cache.tar"

sleep 2

ls -l "$drone_cache_path"

if [ -f "$drone_cache" ];
then
  echo "* found $drone_cache"
  tar xf "$drone_cache" -C /
else
	echo "* did not find $drone_cache"
fi

sleep 2