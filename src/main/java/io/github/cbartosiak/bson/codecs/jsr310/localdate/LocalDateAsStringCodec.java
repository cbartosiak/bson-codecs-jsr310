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

package io.github.cbartosiak.bson.codecs.jsr310.localdate;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code LocalDate} values to and from
 * {@code BSON String}, such as {@code 2018-01-02}.
 * <p>
 * The values are stored as {@code ISO-8601} formatted strings
 * (see {@link LocalDate#toString()}).
 * <p>
 * This type is <b>immutable</b>.
 */
public final class LocalDateAsStringCodec implements Codec<LocalDate> {

    @Override
    public void encode(
            BsonWriter writer,
            LocalDate value,
            EncoderContext encoderContext) {

        requireNonNull(writer, "writer is null");
        requireNonNull(value, "value is null");
        writer.writeString(value.toString());
    }

    @Override
    public LocalDate decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        requireNonNull(reader, "reader is null");
        return translateDecodeExceptions(
                reader::readString,
                LocalDate::parse
        );
    }

    @Override
    public Class<LocalDate> getEncoderClass() {
        return LocalDate.class;
    }
}
