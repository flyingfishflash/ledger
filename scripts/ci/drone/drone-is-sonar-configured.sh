#!/bin/sh

exitcode=0

case $SONAR_HOST in
  (*[![:blank:]]*)
    echo '$SONAR_HOST is not blank';;
  (*)
    echo '$SONAR_HOST contains only blanks or is empty or is unset'
    exitcode=1;;
esac

case $SONAR_LOGIN in
  (*[![:blank:]]*)
    echo '$SONAR_LOGIN is not blank';;
  (*)
    echo '$SONAR_LOGIN contains only blanks or is empty or is unset'
    exitcode=1;;
esac

case $SONAR_PROJECT_KEY in
  (*[![:blank:]]*)
    echo '$SONAR_PROJECT_KEY is not blank';;
  (*)
    echo '$SONAR_PROJECT_KEY contains only blanks or is empty or is unset'
    exitcode=1;;
esac

exit $exitcode