#!/usr/bin/env bash

set -e

for file in `find . -name VERSION`
do
    echo $RELEASE_VERSION > $file
done

./gradlew publishAllTemplates