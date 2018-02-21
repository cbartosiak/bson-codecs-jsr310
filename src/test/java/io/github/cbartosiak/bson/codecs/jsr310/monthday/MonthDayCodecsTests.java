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

import java.time.MonthDay;

import io.github.cbartosiak.bson.codecs.jsr310.AbstractCodecsTests;
import org.junit.jupiter.api.Test;

final class MonthDayCodecsTests
        extends AbstractCodecsTests {

    private MonthDayCodecsTests() {}

    @Test
    void testMonthDayAsDecimal128Codec() {
        MonthDayAsDecimal128Codec monthDayAsDecimal128Codec =
                new MonthDayAsDecimal128Codec();
        testCodec(monthDayAsDecimal128Codec, MonthDay.of(1, 1));
        testCodec(monthDayAsDecimal128Codec, MonthDay.of(12, 31));
        testCodec(monthDayAsDecimal128Codec, MonthDay.now());
    }
}
