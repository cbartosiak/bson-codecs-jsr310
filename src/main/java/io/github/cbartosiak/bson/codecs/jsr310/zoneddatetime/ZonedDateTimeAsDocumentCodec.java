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

package io.github.cbartosiak.bson.codecs.jsr310.zoneddatetime;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.ZonedDateTime.ofStrict;
import static java.util.Collections.unmodifiableMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import io.github.cbartosiak.bson.codecs.jsr310.localdatetime.LocalDateTimeAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneid.ZoneIdAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsInt32Codec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code ZonedDateTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     dateTime: ...,
 *     offset: ...,
 *     zone: ...
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code dateTime} (a non-null value);
 * <li>{@code offset} (a non-null value);
 * <li>{@code zone} (a non-null value).
 * </ul>
 * The field values depend on provided codecs.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class ZonedDateTimeAsDocumentCodec
        implements Codec<ZonedDateTime> {

    private final Codec<LocalDateTime> localDateTimeCodec;
    private final Codec<ZoneOffset>    zoneOffsetCodec;
    private final Codec<ZoneId>        zoneIdCodec;

    private final Map<String, Decoder<?>> fieldDecoders;

    /**
     * Creates a {@code ZonedDateTimeAsDocumentCodec} using:
     * <ul>
     * <li>a {@code LocalDateTimeAsDocumentCodec};
     * <li>a {@code ZoneOffsetAsInt32Codec};
     * <li>a {@code ZoneIdAsStringCodec}.
     * </ul>
     */
    public ZonedDateTimeAsDocumentCodec() {
        this(
                new LocalDateTimeAsDocumentCodec(),
                new ZoneOffsetAsInt32Codec(),
                new ZoneIdAsStringCodec()
        );
    }

    /**
     * Creates an {@code ZonedDateTimeAsDocumentCodec} using
     * the provided codecs.
     */
    public ZonedDateTimeAsDocumentCodec(
            Codec<LocalDateTime> localDateTimeCodec,
            Codec<ZoneOffset> zoneOffsetCodec,
            Codec<ZoneId> zoneIdCodec) {

        this.localDateTimeCodec = localDateTimeCodec;
        this.zoneOffsetCodec = zoneOffsetCodec;
        this.zoneIdCodec = zoneIdCodec;

        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("dateTime", localDateTimeCodec::decode);
        fd.put("offset", zoneOffsetCodec::decode);
        fd.put("zone", zoneIdCodec::decode);
        fieldDecoders = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            ZonedDateTime value,
            EncoderContext encoderContext) {

        writer.writeStartDocument();
        writer.writeName("dateTime");
        localDateTimeCodec.encode(
                writer, value.toLocalDateTime(), encoderContext
        );
        writer.writeName("offset");
        zoneOffsetCodec.encode(writer, value.getOffset(), encoderContext);
        writer.writeName("zone");
        zoneIdCodec.encode(writer, value.getZone(), encoderContext);
        writer.writeEndDocument();
    }

    @Override
    public ZonedDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, fieldDecoders),
                val -> ofStrict(
                        getFieldValue(val, "dateTime", LocalDateTime.class),
                        getFieldValue(val, "offset", ZoneOffset.class),
                        getFieldValue(val, "zone", ZoneId.class)
                )
        );
    }

    @Override
    public Class<ZonedDateTime> getEncoderClass() {
        return ZonedDateTime.class;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        ZonedDateTimeAsDocumentCodec rhs = (ZonedDateTimeAsDocumentCodec)obj;

        return localDateTimeCodec.equals(rhs.localDateTimeCodec) &&
               zoneOffsetCodec.equals(rhs.zoneOffsetCodec) &&
               zoneIdCodec.equals(rhs.zoneIdCodec) &&
               fieldDecoders.equals(rhs.fieldDecoders);
    }

    @Override
    public int hashCode() {
        int result = localDateTimeCodec.hashCode();
        result = 31 * result + zoneOffsetCodec.hashCode();
        result = 31 * result + zoneIdCodec.hashCode();
        result = 31 * result + fieldDecoders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ZonedDateTimeAsDocumentCodec[" +
               "localDateTimeCodec=" + localDateTimeCodec +
               ",zoneOffsetCodec=" + zoneOffsetCodec +
               ",zoneIdCodec=" + zoneIdCodec +
               ",fieldDecoders=" + fieldDecoders +
               ']';
    }
}
