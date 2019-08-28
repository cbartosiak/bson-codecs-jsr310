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

package io.github.cbartosiak.bson.codecs.jsr310.localtime;

import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.LocalTime.MIN;
import static java.time.LocalTime.NOON;
import static java.time.LocalTime.ofNanoOfDay;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class LocalTimeCodecsTests extends AbstractCodecsTests {

    private LocalTimeCodecsTests() {}

    private static void testLocalTimeCodec(
            Codec<LocalTime> codec,
            boolean shouldThrow,
            boolean nanosLost) {

        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        if (shouldThrow) {
            assertThrows(
                    AssertionFailedError.class,
                    () -> testCodec(codec, ofNanoOfDay(10_000_000_100L))
            );
        }
        else {
            testCodec(codec, ofNanoOfDay(10_000_000_100L));
        }
        testCodec(codec, MIN);
        testCodec(codec, MAX.minusNanos(999_999));
        testCodec(codec, MIDNIGHT);
        testCodec(codec, NOON);
        testCodec(codec, now(nanosLost));
    }

    private static LocalTime now(boolean nanosLost) {
        LocalTime now = LocalTime.now();
        if (nanosLost) {
            now = now.minusNanos(now.getNano());
        }
        return now;
    }

    @Test
    void testLocalTimeAsStringCodec() {
        testLocalTimeCodec(new LocalTimeAsStringCodec(), false, false);
    }

    @Test
    void testLocalTimeAsDocumentCodec() {
        testLocalTimeCodec(new LocalTimeAsDocumentCodec(), false, false);
    }

    @Test
    void testLocalTimeAsDateTimeCodec() {
        testLocalTimeCodec(new LocalTimeAsDateTimeCodec(), true, true);
    }

    @Test
    void testLocalTimeAsInt64Codec() {
        testLocalTimeCodec(new LocalTimeAsInt64Codec(), false, false);
    }
}
