name: Check

on: [push, pull_request]
jobs:
  check:
    name: Check
    runs-on: ubuntu-latest
    env:
      GRADLE_OPTS: "-Xmx6g -Xms4g"
      CI: true
    steps:
    - uses: actions/checkout@v1

    - name: Set up JDK 1.8
      uses: actions/setup-java@v1
      with:
        java-version: 1.8

    - uses: actions/cache@v1
      with:
        path: ~/.gradle/caches
        key: \${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle') }}-\${{ hashFiles('**/gradle.properties') }}
        restore-keys: |
          \${{ runner.os }}-gradle-
    - uses: eskatos/gradle-command-action@v1
      with:
        arguments: check --stacktrace
    - name: Show Reports
      uses: actions/upload-artifact@v1
      if: failure()
      with:
         name: reports
         path: build/reports/
