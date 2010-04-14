#!/bin/bash

if [[ $# < 1 ]]; then
    echo "No files given, will run on all .java files."
    echo "Are you sure (y/n)?"
    read CONFIRM
    if [[ $CONFIRM == "y" ]]; then
        find . -name '*.java' | xargs ./$0
    fi
fi

while [[ $# -gt 0 ]]; do
    echo "Processing $1..."
    IMPORTS=$(sed -n '/^import/{s/import.*\.//;s/;$//;p}' $1)
    REMOVED=0
    for x in $IMPORTS; do
        RESULTS=$(grep "\b$x\b" $1 | wc -l)
        if [[ $RESULTS -lt 2 ]]; then
            sed -i "/import .*\.$x;/d" $1
            REMOVED=$(( $REMOVED + 1 ))
        fi
    done
    echo "Removed $REMOVED unused imports."

    ex $1 >/dev/null << HERE
    /import/
    .,/^ *$/-1!sort
    wq
HERE
    echo "Sorted imports."

    shift
done
