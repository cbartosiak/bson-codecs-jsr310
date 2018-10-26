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

package io.github.cbartosiak.bson.codecs.jsr310.localtime;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.LocalTime.of;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code LocalTime} values to and from
 * {@code BSON Document}, such as
 * {@code { hour: 10, minute: 15, second: 30, nano: 0 }}.
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code hour} (a non-null {@code Int32});
 * <li>{@code minute} (a non-null {@code Int32});
 * <li>{@code second} (a non-null {@code Int32});
 * <li>{@code nano} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class LocalTimeAsDocumentCodec implements Codec<LocalTime> {

    private static final Map<String, Decoder<?>> FIELD_DECODERS;

    static {
        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("hour", (r, dc) -> r.readInt32());
        fd.put("minute", (r, dc) -> r.readInt32());
        fd.put("second", (r, dc) -> r.readInt32());
        fd.put("nano", (r, dc) -> r.readInt32());
        FIELD_DECODERS = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            LocalTime value,
            EncoderContext encoderContext) {

        requireNonNull(writer, "writer is null");
        requireNonNull(value, "value is null");
        writer.writeStartDocument();
        writer.writeInt32("hour", value.getHour());
        writer.writeInt32("minute", value.getMinute());
        writer.writeInt32("second", value.getSecond());
        writer.writeInt32("nano", value.getNano());
        writer.writeEndDocument();
    }

    @Override
    public LocalTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        requireNonNull(reader, "reader is null");
        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, FIELD_DECODERS),
                val -> of(
                        getFieldValue(val, "hour", Integer.class),
                        getFieldValue(val, "minute", Integer.class),
                        getFieldValue(val, "second", Integer.class),
                        getFieldValue(val, "nano", Integer.class)
                )
        );
    }

    @Override
    public Class<LocalTime> getEncoderClass() {
        return LocalTime.class;
    }
}
