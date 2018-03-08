#!/usr/bin/env bash

if [ "${TRAVIS_BRANCH}" = "master" ]; then
    openssl aes-256-cbc -K "${encrypted_c6e845ad902f_key}" \
                        -iv "${encrypted_c6e845ad902f_iv}" \
                        -in travis/signing-key.asc.enc \
                        -out travis/signing-key.asc -d
    gpg --fast-import travis/signing-key.asc
fi
