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

package io.github.cbartosiak.bson.codecs.jsr310.zoneoffset;

import static java.time.ZoneOffset.MAX;
import static java.time.ZoneOffset.MIN;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZoneOffset;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class ZoneOffsetCodecsTests extends AbstractCodecsTests {

    private ZoneOffsetCodecsTests() {}

    private static void testZoneOffsetCodec(Codec<ZoneOffset> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        testCodec(codec, MIN);
        testCodec(codec, MAX);
        testCodec(codec, UTC);
    }

    @Test
    void testZoneOffsetAsStringCodec() {
        testZoneOffsetCodec(new ZoneOffsetAsStringCodec());
    }

    @Test
    void testZoneOffsetAsInt32Codec() {
        testZoneOffsetCodec(new ZoneOffsetAsInt32Codec());
    }
}
