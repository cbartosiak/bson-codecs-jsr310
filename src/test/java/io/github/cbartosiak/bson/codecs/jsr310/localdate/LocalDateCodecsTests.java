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

package io.github.cbartosiak.bson.codecs.jsr310.localdate;

import static java.time.LocalDate.MAX;
import static java.time.LocalDate.MIN;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import io.github.cbartosiak.bson.codecs.jsr310.AbstractCodecsTests;
import org.bson.BsonInvalidOperationException;
import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class LocalDateCodecsTests
        extends AbstractCodecsTests {

    private LocalDateCodecsTests() {}

    private static void testLocalDateCodec(
            Codec<LocalDate> codec,
            boolean shouldThrow) {

        if (shouldThrow) {
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
            testCodec(codec, MAX);
        }
        testCodec(codec, now());
    }

    @Test
    void testLocalDateAsStringCodec() {
        testLocalDateCodec(new LocalDateAsStringCodec(), false);
    }

    @Test
    void testLocalDateAsDateTimeCodec() {
        testLocalDateCodec(new LocalDateAsDateTimeCodec(), true);
    }
}
