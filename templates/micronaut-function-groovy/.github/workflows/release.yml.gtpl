name: Release to Production

on:
  push:
    tags:
    - '*'
jobs:
  release:
    name: Release
    runs-on: ubuntu-latest
    env:
      GRADLE_OPTS: "-Xmx6g -Xms4g"
      AWS_ACCESS_KEY_ID: \${{ secrets.PRODUCTION_AWS_ACCESS_KEY_ID }}
      AWS_SECRET_ACCESS_KEY: \${{ secrets.PRODUCTION_AWS_SECRET_ACCESS_KEY }}
      AWS_DEFAULT_REGION: $region
    steps:
    - uses: actions/checkout@v1
    - name: Set up JDK 1.8
      uses: actions/setup-java@v1
      with:
        java-version: 1.8
    - name: Semantic Version
      id: version
      uses: ncipollo/semantic-version-action@v1
    - uses: eskatos/gradle-command-action@v1
      with:
        arguments: deploy --stacktrace -Pversion=\${{ steps.version.outputs.tag }}
