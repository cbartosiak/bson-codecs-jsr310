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

package io.github.cbartosiak.bson.codecs.jsr310.duration;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.unmodifiableMap;

import java.time.Duration;
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
 * Encodes and decodes {@code Duration} values to and from
 * {@code BSON Document}, such as
 * {@code { seconds: 10, nanos: 100 }}.
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code seconds} (a non-null {@code Int64});
 * <li>{@code nanos} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class DurationAsDocumentCodec
        implements Codec<Duration> {

    private static final Map<String, Decoder<?>> FIELD_DECODERS;

    static {
        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("seconds", (r, dc) -> r.readInt64());
        fd.put("nanos", (r, dc) -> r.readInt32());
        FIELD_DECODERS = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            Duration value,
            EncoderContext encoderContext) {

        writer.writeStartDocument();
        writer.writeInt64("seconds", value.getSeconds());
        writer.writeInt32("nanos", value.getNano());
        writer.writeEndDocument();
    }

    @Override
    public Duration decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, FIELD_DECODERS),
                val -> ofSeconds(
                        getFieldValue(val, "seconds", Long.class),
                        getFieldValue(val, "nanos", Integer.class)
                )
        );
    }

    @Override
    public Class<Duration> getEncoderClass() {
        return Duration.class;
    }
}
