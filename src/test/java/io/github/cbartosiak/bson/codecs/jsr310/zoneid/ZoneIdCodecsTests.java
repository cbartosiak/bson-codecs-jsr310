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

package io.github.cbartosiak.bson.codecs.jsr310.zoneid;

import static java.time.ZoneId.getAvailableZoneIds;
import static java.time.ZoneId.of;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZoneId;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class ZoneIdCodecsTests extends AbstractCodecsTests {

    private ZoneIdCodecsTests() {}

    private static void testZoneIdCodec(Codec<ZoneId> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        testCodec(codec, of("Etc/GMT+12"));
        testCodec(codec, of("Etc/GMT-14"));
        testCodec(codec, of("UTC"));
        getAvailableZoneIds().forEach(zoneId -> testCodec(codec, of(zoneId)));
    }

    @Test
    void testZoneIdAsStringCodec() {
        testZoneIdCodec(new ZoneIdAsStringCodec());
    }
}
