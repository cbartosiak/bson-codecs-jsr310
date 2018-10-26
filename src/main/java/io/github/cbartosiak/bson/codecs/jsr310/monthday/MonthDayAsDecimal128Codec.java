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

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateEncodeExceptions;
import static java.lang.String.format;
import static java.time.MonthDay.of;
import static java.util.Objects.requireNonNull;
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
 * Encodes and decodes {@code MonthDay} values to and from
 * {@code BSON Decimal128}, such as {@code 1.02}.
 * <p>
 * The values are stored using the following format: {@code %d.%02d}
 * <ul>
 * <li>the first part represents a month;
 * <li>the latter part represents a day of this month.
 * </ul>
 * This type is <b>immutable</b>.
 */
public final class MonthDayAsDecimal128Codec implements Codec<MonthDay> {

    @Override
    public void encode(
            BsonWriter writer,
            MonthDay value,
            EncoderContext encoderContext) {

        requireNonNull(writer, "writer is null");
        requireNonNull(value, "value is null");
        translateEncodeExceptions(
                () -> value,
                val -> writer.writeDecimal128(parse(format(
                        "%d.%02d",
                        val.getMonthValue(),
                        val.getDayOfMonth()
                )))
        );
    }

    @Override
    public MonthDay decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        requireNonNull(reader, "reader is null");
        return translateDecodeExceptions(
                reader::readDecimal128,
                val -> {
                    BigDecimal bigDecimal = val.bigDecimalValue();
                    int month = bigDecimal.intValue();
                    int day = bigDecimal.subtract(new BigDecimal(month))
                                        .scaleByPowerOfTen(2)
                                        .intValue();
                    return of(month, day);
                }
        );
    }

    @Override
    public Class<MonthDay> getEncoderClass() {
        return MonthDay.class;
    }
}
