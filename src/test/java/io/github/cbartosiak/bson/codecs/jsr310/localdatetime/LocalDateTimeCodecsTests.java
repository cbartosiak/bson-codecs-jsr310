/*
 * Copyright 2018 Cezary Bartosiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cbartosiak.bson.codecs.jsr310.localdatetime;

import static java.time.LocalDateTime.MAX;
import static java.time.LocalDateTime.MIN;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import io.github.cbartosiak.bson.codecs.jsr310.AbstractCodecsTests;
import org.bson.BsonInvalidOperationException;
import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class LocalDateTimeCodecsTests
        extends AbstractCodecsTests {

    private LocalDateTimeCodecsTests() {}

    private static void testLocalDateTimeCodec(
            Codec<LocalDateTime> codec,
            boolean shouldThrow) {

        if (shouldThrow) {
            assertThrows(
                    AssertionFailedError.class,
                    () -> testCodec(codec, ofEpochSecond(10, 100, UTC))
            );
            assertThrows(
                    BsonInvalidOperationException.class,
                    () -> testCodec(codec, MIN)
            );
            assertThrows(
                    BsonInvalidOperationException.class,
                    () -> testCodec(codec, MAX)
            );
        }
        else {
            testCodec(codec, ofEpochSecond(10, 100, UTC));
            testCodec(codec, MIN);
            testCodec(codec, MAX);
        }
        testCodec(codec, now());
    }

    @Test
    void testLocalDateTimeAsStringCodec() {
        testLocalDateTimeCodec(new LocalDateTimeAsStringCodec(), false);
    }

    @Test
    void testLocalDateTimeAsDocumentCodec() {
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(), false);
    }

    @Test
    void testLocalDateTimeAsDateTimeCodec() {
        testLocalDateTimeCodec(new LocalDateTimeAsDateTimeCodec(), true);
    }
}
