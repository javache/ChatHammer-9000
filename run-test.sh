#!/bin/bash

if [[ $# != 1 ]]; then
	echo "Usage: ./run-test.sh fully.classified.className"
	exit 1
fi

to_test=`echo $1 | sed s:\\\.:/:g`.java
ant -Djavac.includes="${to_test}" -Dtest.includes="${to_test}" test-single