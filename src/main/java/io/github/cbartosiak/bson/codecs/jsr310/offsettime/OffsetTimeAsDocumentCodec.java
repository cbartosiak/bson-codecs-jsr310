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

package io.github.cbartosiak.bson.codecs.jsr310.offsettime;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.OffsetTime.of;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsInt32Codec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code OffsetTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     time: ...,
 *     offset: ...
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code time} (a non-null value);
 * <li>{@code offset} (a non-null value).
 * </ul>
 * The field values depend on provided codecs.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class OffsetTimeAsDocumentCodec
        implements Codec<OffsetTime> {

    private final Codec<LocalTime>  localTimeCodec;
    private final Codec<ZoneOffset> zoneOffsetCodec;

    private final Map<String, Decoder<?>> fieldDecoders;

    /**
     * Creates an {@code OffsetTimeAsDocumentCodec} using:
     * <ul>
     * <li>a {@code LocalTimeAsDocumentCodec};
     * <li>a {@code ZoneOffsetAsInt32Codec}.
     * </ul>
     */
    public OffsetTimeAsDocumentCodec() {
        this(
                new LocalTimeAsDocumentCodec(),
                new ZoneOffsetAsInt32Codec()
        );
    }

    /**
     * Creates an {@code OffsetTimeAsDocumentCodec} using
     * the provided codecs.
     *
     * @param localTimeCodec  not null
     * @param zoneOffsetCodec not null
     */
    public OffsetTimeAsDocumentCodec(
            Codec<LocalTime> localTimeCodec,
            Codec<ZoneOffset> zoneOffsetCodec) {

        this.localTimeCodec = requireNonNull(localTimeCodec);
        this.zoneOffsetCodec = requireNonNull(zoneOffsetCodec);

        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("time", localTimeCodec::decode);
        fd.put("offset", zoneOffsetCodec::decode);
        fieldDecoders = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            OffsetTime value,
            EncoderContext encoderContext) {

        writer.writeStartDocument();
        writer.writeName("time");
        localTimeCodec.encode(writer, value.toLocalTime(), encoderContext);
        writer.writeName("offset");
        zoneOffsetCodec.encode(writer, value.getOffset(), encoderContext);
        writer.writeEndDocument();
    }

    @Override
    public OffsetTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, fieldDecoders),
                val -> of(
                        getFieldValue(val, "time", LocalTime.class),
                        getFieldValue(val, "offset", ZoneOffset.class)
                )
        );
    }

    @Override
    public Class<OffsetTime> getEncoderClass() {
        return OffsetTime.class;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        OffsetTimeAsDocumentCodec rhs = (OffsetTimeAsDocumentCodec)obj;

        return localTimeCodec.equals(rhs.localTimeCodec) &&
               zoneOffsetCodec.equals(rhs.zoneOffsetCodec) &&
               fieldDecoders.equals(rhs.fieldDecoders);
    }

    @Override
    public int hashCode() {
        int result = localTimeCodec.hashCode();
        result = 31 * result + zoneOffsetCodec.hashCode();
        result = 31 * result + fieldDecoders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OffsetTimeAsDocumentCodec[" +
               "localTimeCodec=" + localTimeCodec +
               ",zoneOffsetCodec=" + zoneOffsetCodec +
               ",fieldDecoders=" + fieldDecoders +
               ']';
    }
}
