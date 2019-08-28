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
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.bson.BsonInvalidOperationException;
import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;
import io.github.cbartosiak.bson.codecs.jsr310.localdate.LocalDateAsDateTimeCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localdate.LocalDateAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localdate.LocalDateAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDateTimeCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsInt64Codec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsStringCodec;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class LocalDateTimeCodecsTests extends AbstractCodecsTests {

    private LocalDateTimeCodecsTests() {}

    private static void testLocalDateTimeCodec(
            Codec<LocalDateTime> codec,
            boolean shouldThrowForDate,
            boolean shouldThrowForTime,
            boolean nanosLost) {

        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        if (shouldThrowForDate) {
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
            testCodec(codec, MIN);
            if (shouldThrowForTime) {
                assertThrows(
                        AssertionFailedError.class,
                        () -> testCodec(codec, MAX)
                );
            }
            else {
                testCodec(codec, MAX);
            }
        }

        if (shouldThrowForTime) {
            assertThrows(
                    AssertionFailedError.class,
                    () -> testCodec(codec, ofEpochSecond(10, 100, UTC))
            );
        }
        else {
            testCodec(codec, ofEpochSecond(10, 100, UTC));
        }

        testCodec(codec, now(nanosLost));
    }

    private static LocalDateTime now(boolean nanosLost) {
        LocalDateTime now = LocalDateTime.now();
        if (nanosLost) {
            now = now.minusNanos(now.getNano());
        }
        return now;
    }

    @Test
    void testLocalDateTimeAsStringCodec() {
        testLocalDateTimeCodec(
                new LocalDateTimeAsStringCodec(),
                false, false, false
        );
    }

    @Test
    void testLocalDateTimeAsDocumentCodec() {
        testLocalDateTimeCodec(
                new LocalDateTimeAsDocumentCodec(),
                false, false, false
        );
        assertThrows(
                NullPointerException.class,
                () -> new LocalDateTimeAsDocumentCodec(
                        null, new LocalTimeAsStringCodec()
                )
        );
        assertThrows(
                NullPointerException.class,
                () -> new LocalDateTimeAsDocumentCodec(
                        new LocalDateAsStringCodec(), null
                )
        );
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsStringCodec(),
                new LocalTimeAsStringCodec()
        ), false, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsStringCodec(),
                new LocalTimeAsDocumentCodec()
        ), false, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsStringCodec(),
                new LocalTimeAsDateTimeCodec()
        ), false, true, true);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsStringCodec(),
                new LocalTimeAsInt64Codec()
        ), false, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDocumentCodec(),
                new LocalTimeAsStringCodec()
        ), false, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDocumentCodec(),
                new LocalTimeAsDocumentCodec()
        ), false, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDocumentCodec(),
                new LocalTimeAsDateTimeCodec()
        ), false, true, true);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDocumentCodec(),
                new LocalTimeAsInt64Codec()
        ), false, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDateTimeCodec(),
                new LocalTimeAsStringCodec()
        ), true, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDateTimeCodec(),
                new LocalTimeAsDocumentCodec()
        ), true, false, false);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDateTimeCodec(),
                new LocalTimeAsDateTimeCodec()
        ), true, true, true);
        testLocalDateTimeCodec(new LocalDateTimeAsDocumentCodec(
                new LocalDateAsDateTimeCodec(),
                new LocalTimeAsInt64Codec()
        ), true, false, false);
    }

    @Test
    void testLocalDateTimeAsDateTimeCodec() {
        testLocalDateTimeCodec(
                new LocalDateTimeAsDateTimeCodec(),
                true, true, true
        );
    }
}
