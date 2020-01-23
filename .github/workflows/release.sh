#!/usr/bin/env bash

for file in `find . -name VERSION`
do
    echo $RELEASE_VERSION > $file
done

./gradlew publishAllTemplates