#!/bin/sh
gradle version -x bootBuildInfo | grep Version | awk '{ print $2 }' | tr -d "\n"