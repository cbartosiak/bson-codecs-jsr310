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

package io.github.cbartosiak.bson.codecs.jsr310.zoneddatetime;

import static java.time.ZonedDateTime.now;
import static java.time.ZonedDateTime.of;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;
import io.github.cbartosiak.bson.codecs.jsr310.localdatetime.LocalDateTimeAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneid.ZoneIdAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsInt32Codec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsStringCodec;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class ZonedDateTimeCodecsTests extends AbstractCodecsTests {

    private ZonedDateTimeCodecsTests() {}

    private static void testZonedDateTimeCodec(Codec<ZonedDateTime> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        testCodec(codec, of(
                Year.MIN_VALUE, 1, 1, 0, 0, 0, 0, ZoneId.of("Etc/GMT+12")
        ));
        testCodec(codec, of(
                Year.MAX_VALUE, 12, 31, 23, 59, 59, 999, ZoneId.of("Etc/GMT-14")
        ));
        testCodec(codec, now());
        testCodec(codec, now(ZoneId.of("UTC")));
    }

    @Test
    void testZonedDateTimeAsStringCodec() {
        testZonedDateTimeCodec(new ZonedDateTimeAsStringCodec());
    }

    @Test
    void testZonedDateTimeAsDocumentCodec() {
        testZonedDateTimeCodec(new ZonedDateTimeAsDocumentCodec());
        assertThrows(
                NullPointerException.class,
                () -> new ZonedDateTimeAsDocumentCodec(
                        null,
                        new ZoneOffsetAsStringCodec(),
                        new ZoneIdAsStringCodec()
                )
        );
        assertThrows(
                NullPointerException.class,
                () -> new ZonedDateTimeAsDocumentCodec(
                        new LocalDateTimeAsDocumentCodec(),
                        null,
                        new ZoneIdAsStringCodec()
                )
        );
        assertThrows(
                NullPointerException.class,
                () -> new ZonedDateTimeAsDocumentCodec(
                        new LocalDateTimeAsDocumentCodec(),
                        new ZoneOffsetAsStringCodec(),
                        null
                )
        );
        testZonedDateTimeCodec(new ZonedDateTimeAsDocumentCodec(
                new LocalDateTimeAsDocumentCodec(),
                new ZoneOffsetAsStringCodec(),
                new ZoneIdAsStringCodec()
        ));
        testZonedDateTimeCodec(new ZonedDateTimeAsDocumentCodec(
                new LocalDateTimeAsDocumentCodec(),
                new ZoneOffsetAsInt32Codec(),
                new ZoneIdAsStringCodec()
        ));
    }
}
