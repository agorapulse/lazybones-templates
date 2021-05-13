#!/usr/bin/env bash

set -e

for file in `find . -name VERSION`
do
    echo $RELEASE_VERSION > $file
done

export LAZYBONES_REPO_DIR=/tmp/lazybones
git clone https://github.com/agorapulse/lazybones "$LAZYBONES_REPO_DIR"

$LAZYBONES_REPO_DIR/gradlew -p "$LAZYBONES_REPO_DIR" install installDist -x test -x integTest

export PATH="$PATH:$LAZYBONES_REPO_DIR/lazybones-app/build/install/lazybones/bin"

./gradlew publishAllTemplates

uploadFile "Kordamp Groovy Template" "build/packages/kordamp-groovy-template-$RELEASE_VERSION.zip"
uploadFile "Micronaut Function Groovy Template" "build/packages/micronaut-function-groovy-template-$RELEASE_VERSION.zip"

function uploadFile() {
    curl \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: $(file -b --mime-type $2)" \
    --data-binary @$2 \
    "$UPLOAD_URL?name=$(basename $2)&label=$1"
}
