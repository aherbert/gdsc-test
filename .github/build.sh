#!/bin/sh
ARGS=""
if mvn help:evaluate -Dexpression=project.version -q -DforceStdout | grep SNAPSHOT; then
    ARGS="-P snapshot-release --settings deploy/settings.xml deploy"
fi

mvn -V -B --no-transfer-progress -P jacoco spotbugs:check verify $ARGS
