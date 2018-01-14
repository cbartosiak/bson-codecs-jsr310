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
import static java.time.Duration.ofSeconds;
import static org.bson.types.Decimal128.parse;

import java.math.BigDecimal;
import java.time.Duration;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code Duration} objects to and from {@code Decimal128},
 * such as {@code 1000.000000100}.
 * <p>
 * Objects are stored in {@code %d.%09d} format, where the first part means
 * seconds and the latter nanoseconds.
 * <p>
 * The implementation is <b>thread-safe</b>.
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">
 * BSON Types
 * </a>
 */
public final class DurationCodec
        implements Codec<Duration> {

    @Override
    public void encode(
            BsonWriter writer,
            Duration value,
            EncoderContext encoderContext) {

        writer.writeDecimal128(parse(format(
                "%d.%09d",
                value.getSeconds(),
                value.getNano()
        )));
    }

    @Override
    public Duration decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        BigDecimal value = reader
                .readDecimal128()
                .bigDecimalValue();
        long seconds = value.longValue();
        return ofSeconds(
                seconds,
                value.subtract(new BigDecimal(seconds))
                     .scaleByPowerOfTen(9)
                     .abs()
                     .intValue()
        );
    }

    @Override
    public Class<Duration> getEncoderClass() {
        return Duration.class;
    }
}
