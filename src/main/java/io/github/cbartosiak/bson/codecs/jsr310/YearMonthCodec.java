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
 * such as {@code 2017.10}.
 * </p>
 * <p>
 * Objects are stored in {@code %d.%02d} format, where the first part means
 * a year and the latter a month.
 * </p>
 * <p>
 * The implementation is <b>thread-safe</b>.
 * </p>
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">
 * BSON Types
 * </a>
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
