#!/usr/bin/env bash

if [ "${TRAVIS_BRANCH}" = "master" ]; then
    mvn deploy -P build-extras,sign -B --settings travis/settings.xml
fi
