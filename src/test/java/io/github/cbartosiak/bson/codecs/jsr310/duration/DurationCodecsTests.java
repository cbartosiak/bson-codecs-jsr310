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

package io.github.cbartosiak.bson.codecs.jsr310.duration;

import static java.lang.Long.MAX_VALUE;
import static java.time.Duration.ZERO;
import static java.time.Duration.ofHours;
import static java.time.Duration.ofSeconds;

import java.time.Duration;

import io.github.cbartosiak.bson.codecs.jsr310.AbstractCodecsTests;
import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class DurationCodecsTests
        extends AbstractCodecsTests {

    private DurationCodecsTests() {}

    private static void testDurationCodec(Codec<Duration> codec) {
        testCodec(codec, ZERO);
        testCodec(codec, ofSeconds(MAX_VALUE, 999_999_999L));
        testCodec(codec, ofHours(12));
    }

    @Test
    void testDurationAsStringCodec() {
        testDurationCodec(new DurationAsStringCodec());
    }

    @Test
    void testDurationAsDocumentCodec() {
        testDurationCodec(new DurationAsDocumentCodec());
    }

    @Test
    void testDurationAsDecimal128Codec() {
        testDurationCodec(new DurationAsDecimal128Codec());
    }
}
