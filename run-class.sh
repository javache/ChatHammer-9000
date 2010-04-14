#!/bin/bash

if [[ $# != 1 ]]; then
	echo "Usage: ./run-class.sh fully.classified.className"
	exit 1
fi

CLASSPATH=$(find lib -name '*.jar' | awk -vORS=':' "{print}")
CLASSPATH="$CLASSPATH""build/classes:build/test/classes"

java -cp "$CLASSPATH" $1
