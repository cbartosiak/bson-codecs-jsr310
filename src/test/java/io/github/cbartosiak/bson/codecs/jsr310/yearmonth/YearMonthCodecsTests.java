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

package io.github.cbartosiak.bson.codecs.jsr310.yearmonth;

import static java.time.YearMonth.now;
import static java.time.YearMonth.of;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Year;
import java.time.YearMonth;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class YearMonthCodecsTests extends AbstractCodecsTests {

    private YearMonthCodecsTests() {}

    private static void testYearMonthCodec(Codec<YearMonth> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        testCodec(codec, of(Year.MIN_VALUE, 1));
        testCodec(codec, of(Year.MAX_VALUE, 12));
        testCodec(codec, now());
    }

    @Test
    void testYearMonthAsStringCodec() {
        testYearMonthCodec(new YearMonthAsStringCodec());
    }

    @Test
    void testYearMonthAsDocumentCodec() {
        testYearMonthCodec(new YearMonthAsDocumentCodec());
    }

    @Test
    void testYearMonthAsDecimal128Codec() {
        testYearMonthCodec(new YearMonthAsDecimal128Codec());
    }
}
