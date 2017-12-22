#!/usr/bin/env bash
if [[ $TRAVIS_BRANCH =~ ^(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)$ ]] && [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    mvn deploy -P build-extras,sign -B --settings travis/settings.xml
fi
