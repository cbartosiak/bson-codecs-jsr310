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

import java.time.Period;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code Period} values to and from
 * {@code BSON String}, such as {@code P6Y3M1D}.
 * <p>
 * Values are stored in ISO-8601 period format. A zero period is represented as
 * zero days, {@code P0D}.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class PeriodCodec
        implements Codec<Period> {

    @Override
    public void encode(
            BsonWriter writer,
            Period value,
            EncoderContext encoderContext) {

        writer.writeString(value.toString());
    }

    @Override
    public Period decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                reader::readString,
                Period::parse
        );
    }

    @Override
    public Class<Period> getEncoderClass() {
        return Period.class;
    }
}
