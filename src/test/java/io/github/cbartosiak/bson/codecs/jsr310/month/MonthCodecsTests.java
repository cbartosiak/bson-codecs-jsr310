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

package io.github.cbartosiak.bson.codecs.jsr310.month;

import static java.time.Month.values;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Month;

import org.bson.codecs.Codec;
import org.junit.jupiter.api.Test;

import io.github.cbartosiak.bson.codecs.jsr310.internal.AbstractCodecsTests;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
final class MonthCodecsTests extends AbstractCodecsTests {

    private MonthCodecsTests() {}

    private static void testMonthCodec(Codec<Month> codec) {
        assertThrows(
                NullPointerException.class,
                () -> testCodec(codec, null)
        );
        for (Month month : values()) {
            testCodec(codec, month);
        }
    }

    @Test
    void testMonthAsStringCodec() {
        testMonthCodec(new MonthAsStringCodec());
    }

    @Test
    void testMonthAsInt32Codec() {
        testMonthCodec(new MonthAsInt32Codec());
    }
}
