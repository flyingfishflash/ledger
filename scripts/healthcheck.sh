#!/bin/bash

#curl --silent --fail http://127.0.0.1:8181/actuator/info
#wget http://127.0.0.1:8181/actuator/info -T 30 -q -O -

if wget http://127.0.0.1:8181/actuator/info -T 30 -q -O -
then
  	exit 0
fi

exit 1
