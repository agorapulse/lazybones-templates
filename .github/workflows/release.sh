#!/usr/bin/env bash

for file in `find . -name VERSION`
do
    cat $RELEASE_VERSION > $file
done

./gradlew publishAllTemplates