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

package io.github.cbartosiak.bson.codecs.jsr310.dayofweek;

import static java.time.DayOfWeek.values;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class DayOfWeekCodecsTests extends AbstractCodecsTests {

    private DayOfWeekCodecsTests() {}

    private static void testDayOfWeekCodec(Codec<DayOfWeek> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        for (DayOfWeek dayOfWeek : values()) {
            testCodec(codec, dayOfWeek);
        }
    }

    @Test
    void testDayOfWeekAsStringCodec() {
        testDayOfWeekCodec(new DayOfWeekAsStringCodec());
    }

    @Test
    void testDayOfWeekAsInt32Codec() {
        testDayOfWeekCodec(new DayOfWeekAsInt32Codec());
    }
}
