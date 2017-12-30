#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    mvn deploy -P build-extras,sign -B --settings travis/settings.xml
fi
