#!/usr/bin/env bash

curl -s "https://get.sdkman.io" | bash
sdk install lazybones

./gradlew installAllTemplates

cd /tmp

lazybones create kordamp-groovy 1.1.0-SNAPSHOT kordamp-groovy-test \
 -Pid=kordamp-groovy-test \
 -Porg=agorapulse \
 -Pgroup=com.agorapulse\
 -Ppkg=com.agorapulse.kordamp.groovy.test\
 -Pname="Kordamp Groovy"\
 -Pdesc="Testing"\
 -Pvendor="Agorapulse"\
 -Pdev.name="Vladimir Orany"\
 -Pdev.id=musketyr\
 -Pbintray.org=agorapulse\
 -Pbintray.repo=libs\

