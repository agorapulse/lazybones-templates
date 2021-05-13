#!/usr/bin/env bash

set -e

for file in `find . -name VERSION`
do
    echo $RELEASE_VERSION > $file
done

./gradlew publishAllTemplates

uploadFile "build/packages/kordamp-groovy-template-$RELEASE_VERSION.zip"
uploadFile "build/packages/micronaut-function-groovy-template-$RELEASE_VERSION.zip"

function uploadFile() {
    curl \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: $(file -b --mime-type $1)" \
    --data-binary @$1 \
    "$UPLOAD_URL?name=$(basename $1)"
}
