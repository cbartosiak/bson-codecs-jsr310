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

package io.github.cbartosiak.bson.codecs.jsr310;

import static java.lang.String.format;
import static java.time.YearMonth.of;
import static org.bson.types.Decimal128.parse;

import java.math.BigDecimal;
import java.time.YearMonth;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code YearMonth} objects to and from {@code Decimal128},
 * such as {@code 2018.01}.
 * <p>
 * Objects are stored in {@code %d.%02d} format, where the first part means
 * a year and the latter a month.
 * <p>
 * The implementation is <b>thread-safe</b>.
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">
 * BSON Types</a>
 */
public final class YearMonthCodec
        implements Codec<YearMonth> {

    @Override
    public void encode(
            BsonWriter writer,
            YearMonth value,
            EncoderContext encoderContext) {

        writer.writeDecimal128(parse(format(
                "%d.%02d",
                value.getYear(),
                value.getMonthValue()
        )));
    }

    @Override
    public YearMonth decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        BigDecimal value = reader
                .readDecimal128()
                .bigDecimalValue();
        int year = value.intValue();
        return of(
                year,
                value.subtract(new BigDecimal(year))
                     .scaleByPowerOfTen(2)
                     .abs()
                     .intValue()
        );
    }

    @Override
    public Class<YearMonth> getEncoderClass() {
        return YearMonth.class;
    }
}
