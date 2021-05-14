#!/bin/sh

drone_cache="/drone/drone_cache/$1.cache.tar"

if [ -f "$drone_cache" ];
then
  echo "* found $drone_cache"
  tar xf "$drone_cache" -C /
else
	echo "* did not find $drone_cache"
fi
