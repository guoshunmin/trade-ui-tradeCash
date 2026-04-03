#!/bin/sh

# Maven Wrapper Script
MAVEN_VERSION=3.8.1

if [ "x$MAVEN_HOME" = "x" ]; then
  MAVEN_HOME=$(dirname "$0")/mvnw
fi

exec "$MAVEN_HOME" "$@"