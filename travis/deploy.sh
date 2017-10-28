#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" =~ ^v[0-9]\.[0-9]\.[0-9]$ ] && [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    mvn deploy -P build-extras,sign --settings travis/settings.xml
fi
