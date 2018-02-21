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

import java.time.Year;
import java.time.YearMonth;

import io.github.cbartosiak.bson.codecs.jsr310.AbstractCodecsTests;
import org.junit.jupiter.api.Test;

final class YearMonthCodecsTests
        extends AbstractCodecsTests {

    private YearMonthCodecsTests() {}

    @Test
    void testYearMonthAsDecimal128Codec() {
        YearMonthAsDecimal128Codec yearMonthAsDecimal128Codec =
                new YearMonthAsDecimal128Codec();
        testCodec(yearMonthAsDecimal128Codec, YearMonth.of(Year.MIN_VALUE, 1));
        testCodec(yearMonthAsDecimal128Codec, YearMonth.of(Year.MAX_VALUE, 12));
        testCodec(yearMonthAsDecimal128Codec, YearMonth.now());
    }
}
