/*
 * MIT License
 *
 * Copyright (c) 2017 Cezary Bartosiak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.cbartosiak.bson.codecs.jsr310;

import static java.lang.String.format;
import static java.time.MonthDay.of;
import static org.bson.types.Decimal128.parse;

import java.math.BigDecimal;
import java.time.MonthDay;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code MonthDay} objects to and from {@code Decimal128},
 * such as {@code 2.15}.
 * </p>
 * <p>
 * Objects are stored in {@code %d.%02d} format, where the first part means
 * a month and the latter a day of the month.
 * </p>
 * <p>
 * The implementation is <b>thread-safe</b>.
 * </p>
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">
 * BSON Types
 * </a>
 */
public final class MonthDayCodec
        implements Codec<MonthDay> {

    @Override
    public void encode(
            BsonWriter writer,
            MonthDay value,
            EncoderContext encoderContext) {

        writer.writeDecimal128(parse(format(
                "%d.%02d",
                value.getMonthValue(),
                value.getDayOfMonth()
        )));
    }

    @Override
    public MonthDay decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        BigDecimal value = reader
                .readDecimal128()
                .bigDecimalValue();
        int month = value.intValue();
        return of(
                month,
                value.subtract(new BigDecimal(month))
                     .scaleByPowerOfTen(2)
                     .intValue()
        );
    }

    @Override
    public Class<MonthDay> getEncoderClass() {
        return MonthDay.class;
    }
}
