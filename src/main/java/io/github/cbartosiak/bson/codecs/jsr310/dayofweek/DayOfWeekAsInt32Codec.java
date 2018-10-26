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

package io.github.cbartosiak.bson.codecs.jsr310.dayofweek;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code DayOfWeek} values to and from
 * {@code BSON Int32}, such as {@code 2}.
 * <p>
 * The values are stored as {@code ISO-8601} integers
 * (see {@link DayOfWeek#getValue()}).
 * <p>
 * This type is <b>immutable</b>.
 */
public final class DayOfWeekAsInt32Codec implements Codec<DayOfWeek> {

    @Override
    public void encode(
            BsonWriter writer,
            DayOfWeek value,
            EncoderContext encoderContext) {

        requireNonNull(writer, "writer is null");
        requireNonNull(value, "value is null");
        writer.writeInt32(value.getValue());
    }

    @Override
    public DayOfWeek decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        requireNonNull(reader, "reader is null");
        return translateDecodeExceptions(
                reader::readInt32,
                DayOfWeek::of
        );
    }

    @Override
    public Class<DayOfWeek> getEncoderClass() {
        return DayOfWeek.class;
    }
}
