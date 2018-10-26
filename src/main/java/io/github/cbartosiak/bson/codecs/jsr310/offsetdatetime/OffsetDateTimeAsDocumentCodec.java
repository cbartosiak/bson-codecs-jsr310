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

package io.github.cbartosiak.bson.codecs.jsr310.offsetdatetime;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.OffsetDateTime.of;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import io.github.cbartosiak.bson.codecs.jsr310.localdatetime.LocalDateTimeAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsInt32Codec;

/**
 * <p>
 * Encodes and decodes {@code OffsetDateTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     dateTime: ...,
 *     offset: ...
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code dateTime} (a non-null value);
 * <li>{@code offset} (a non-null value).
 * </ul>
 * The field values depend on provided codecs.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class OffsetDateTimeAsDocumentCodec
        implements Codec<OffsetDateTime> {

    private final Codec<LocalDateTime> localDateTimeCodec;
    private final Codec<ZoneOffset>    zoneOffsetCodec;

    private final Map<String, Decoder<?>> fieldDecoders;

    /**
     * Creates an {@code OffsetDateTimeAsDocumentCodec} using:
     * <ul>
     * <li>a {@link LocalDateTimeAsDocumentCodec};
     * <li>a {@link ZoneOffsetAsInt32Codec}.
     * </ul>
     */
    public OffsetDateTimeAsDocumentCodec() {
        this(
                new LocalDateTimeAsDocumentCodec(),
                new ZoneOffsetAsInt32Codec()
        );
    }

    /**
     * Creates an {@code OffsetDateTimeAsDocumentCodec} using
     * the provided codecs.
     *
     * @param localDateTimeCodec not null
     * @param zoneOffsetCodec    not null
     */
    public OffsetDateTimeAsDocumentCodec(
            Codec<LocalDateTime> localDateTimeCodec,
            Codec<ZoneOffset> zoneOffsetCodec) {

        this.localDateTimeCodec = requireNonNull(
                localDateTimeCodec, "localDateTimeCodec is null"
        );
        this.zoneOffsetCodec = requireNonNull(
                zoneOffsetCodec, "zoneOffsetCodec is null"
        );

        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("dateTime", localDateTimeCodec::decode);
        fd.put("offset", zoneOffsetCodec::decode);
        fieldDecoders = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            OffsetDateTime value,
            EncoderContext encoderContext) {

        requireNonNull(writer, "writer is null");
        requireNonNull(value, "value is null");

        writer.writeStartDocument();

        writer.writeName("dateTime");
        localDateTimeCodec.encode(
                writer, value.toLocalDateTime(), encoderContext
        );

        writer.writeName("offset");
        zoneOffsetCodec.encode(writer, value.getOffset(), encoderContext);

        writer.writeEndDocument();
    }

    @Override
    public OffsetDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        requireNonNull(reader, "reader is null");
        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, fieldDecoders),
                val -> of(
                        getFieldValue(val, "dateTime", LocalDateTime.class),
                        getFieldValue(val, "offset", ZoneOffset.class)
                )
        );
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        OffsetDateTimeAsDocumentCodec rhs = (OffsetDateTimeAsDocumentCodec)obj;

        return localDateTimeCodec.equals(rhs.localDateTimeCodec) &&
               zoneOffsetCodec.equals(rhs.zoneOffsetCodec) &&
               fieldDecoders.equals(rhs.fieldDecoders);
    }

    @Override
    public int hashCode() {
        int result = localDateTimeCodec.hashCode();
        result = 31 * result + zoneOffsetCodec.hashCode();
        result = 31 * result + fieldDecoders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OffsetDateTimeAsDocumentCodec[" +
               "localDateTimeCodec=" + localDateTimeCodec +
               ",zoneOffsetCodec=" + zoneOffsetCodec +
               ",fieldDecoders=" + fieldDecoders +
               ']';
    }
}
