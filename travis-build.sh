#!/bin/bash
set -e
./gradlew signArchives
#./gradlew clean build uploadArchives
#if [[ $TRAVIS_BRANCH =~ ^[0-9].*$ ]]; then
#	echo "Releasing to maven central"
#	./gradlew nexusStagingRelease
#else
#	echo "Snapshot build"
#fi
