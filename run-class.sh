#!/bin/bash

if [[ $# != 1 ]]; then
	echo "Usage: ./run-class.sh fully.classified.className"
	exit 1
fi

ant -Drun.class=$1 -Djavac.includes= run-single
