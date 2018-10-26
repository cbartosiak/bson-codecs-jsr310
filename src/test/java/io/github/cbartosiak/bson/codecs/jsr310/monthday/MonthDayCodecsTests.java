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

package io.github.cbartosiak.bson.codecs.jsr310.monthday;

import static java.time.MonthDay.now;
import static java.time.MonthDay.of;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.MonthDay;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class MonthDayCodecsTests extends AbstractCodecsTests {

    private MonthDayCodecsTests() {}

    private static void testMonthDayCodec(Codec<MonthDay> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        testCodec(codec, of(1, 1));
        testCodec(codec, of(12, 31));
        testCodec(codec, now());
    }

    @Test
    void testMonthDayAsStringCodec() {
        testMonthDayCodec(new MonthDayAsStringCodec());
    }

    @Test
    void testMonthDayAsDocumentCodec() {
        testMonthDayCodec(new MonthDayAsDocumentCodec());
    }

    @Test
    void testMonthDayAsDecimal128Codec() {
        testMonthDayCodec(new MonthDayAsDecimal128Codec());
    }
}
