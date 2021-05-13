#!/usr/bin/env bash

function uploadFile() {
    echo "label: $1"
    echo "file: $2"
    curl \
    --data-urlencode "name=$(basename $2)" \
    --data-urlencode "label=1" \
    -H "Authorization: token $GITHUB_TOKEN" \
    -H "Content-Type: $(file -b --mime-type $2)" \
    --data-binary @$2 \
    "$UPLOAD_URL"
}

set -e
set -x

for file in `find . -name VERSION`
do
    echo $RELEASE_VERSION > $file
done

export LAZYBONES_REPO_DIR=/tmp/lazybones
git clone https://github.com/agorapulse/lazybones "$LAZYBONES_REPO_DIR"

$LAZYBONES_REPO_DIR/gradlew -p "$LAZYBONES_REPO_DIR" install installDist -x test -x integTest

export PATH="$PATH:$LAZYBONES_REPO_DIR/lazybones-app/build/install/lazybones/bin"

./gradlew packageAllTemplates

uploadFile "Kordamp Groovy Template" "build/packages/kordamp-groovy-template-$RELEASE_VERSION.zip"
uploadFile "Micronaut Function Groovy Template" "build/packages/micronaut-function-groovy-template-$RELEASE_VERSION.zip"
