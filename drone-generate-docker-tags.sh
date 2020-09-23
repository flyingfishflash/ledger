#!/bin/sh
gradle --no-daemon version | grep Version | awk '{ print $2 }'