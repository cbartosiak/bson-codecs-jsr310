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

import static io.github.cbartosiak.bson.codecs.jsr310.ExceptionsUtil.translateDecodeExceptions;
import static io.github.cbartosiak.bson.codecs.jsr310.ExceptionsUtil.translateEncodeExceptions;
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
 * Encodes and decodes {@code Duration} values to and from
 * {@code BSON Decimal128}, such as {@code 10.100000000}.
 * <p>
 * Values are stored in {@code %d.%09d} format, where the first part represents
 * seconds and the latter nanoseconds.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class DurationCodec
        implements Codec<Duration> {

    @Override
    public void encode(
            BsonWriter writer,
            Duration value,
            EncoderContext encoderContext) {

        translateEncodeExceptions(
                () -> value,
                val -> writer.writeDecimal128(parse(format(
                        "%d.%09d",
                        val.getSeconds(),
                        val.getNano()
                )))
        );
    }

    @Override
    public Duration decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        //noinspection OverlyLongLambda
        return translateDecodeExceptions(
                reader::readDecimal128,
                val -> {
                    BigDecimal bigDecimal = val.bigDecimalValue();
                    long seconds = bigDecimal.longValue();
                    int nanos = bigDecimal.subtract(new BigDecimal(seconds))
                                          .scaleByPowerOfTen(9)
                                          .abs()
                                          .intValue();
                    return ofSeconds(seconds, nanos);
                }
        );
    }

    @Override
    public Class<Duration> getEncoderClass() {
        return Duration.class;
    }
}
