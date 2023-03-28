#!/bin/sh
ARGS=""
if mvn help:evaluate -Dexpression=project.version -q -DforceStdout | grep SNAPSHOT; then
    ARGS="-P snapshot-release --settings deploy/settings.xml deploy"
fi

mvn -V -B --no-transfer-progress -P jacoco,gdsc-test-doc,gdsc-test-examples spotbugs:check verify $ARGS
