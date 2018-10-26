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

package io.github.cbartosiak.bson.codecs.jsr310.offsettime;

import static java.time.OffsetTime.MAX;
import static java.time.OffsetTime.MIN;
import static java.time.OffsetTime.now;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetTime;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsInt32Codec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsStringCodec;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class OffsetTimeCodecsTests extends AbstractCodecsTests {

    private OffsetTimeCodecsTests() {}

    private static void testOffsetTimeCodec(Codec<OffsetTime> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        testCodec(codec, MIN);
        testCodec(codec, MAX);
        testCodec(codec, now());
    }

    @Test
    void testOffsetTimeAsStringCodec() {
        testOffsetTimeCodec(new OffsetTimeAsStringCodec());
    }

    @Test
    void testOffsetTimeAsDocumentCodec() {
        testOffsetTimeCodec(new OffsetTimeAsDocumentCodec());
        assertThrows(
                NullPointerException.class,
                () -> new OffsetTimeAsDocumentCodec(
                        null, new ZoneOffsetAsStringCodec()
                )
        );
        assertThrows(
                NullPointerException.class,
                () -> new OffsetTimeAsDocumentCodec(
                        new LocalTimeAsDocumentCodec(), null
                )
        );
        testOffsetTimeCodec(new OffsetTimeAsDocumentCodec(
                new LocalTimeAsDocumentCodec(), new ZoneOffsetAsStringCodec()
        ));
        testOffsetTimeCodec(new OffsetTimeAsDocumentCodec(
                new LocalTimeAsDocumentCodec(), new ZoneOffsetAsInt32Codec()
        ));
    }
}
