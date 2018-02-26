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

package io.github.cbartosiak.bson.codecs.jsr310.localdatetime;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.LocalDateTime.of;
import static java.util.Collections.unmodifiableMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import io.github.cbartosiak.bson.codecs.jsr310.localdate.LocalDateAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDocumentCodec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code LocalDateTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     date: ...,
 *     time: ...
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code date} (a non-null value);
 * <li>{@code time} (a non-null value).
 * </ul>
 * The field values depend on provided codecs.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class LocalDateTimeAsDocumentCodec
        implements Codec<LocalDateTime> {

    private final Codec<LocalDate> localDateCodec;
    private final Codec<LocalTime> localTimeCodec;

    private final Map<String, Decoder<?>> fieldDecoders;

    /**
     * Creates a {@code LocalDateTimeAsDocumentCodec} using:
     * <ul>
     * <li>a {@code LocalDateAsDocumentCodec};
     * <li>a {@code LocalTimeAsDocumentCodec}.
     * </ul>
     */
    public LocalDateTimeAsDocumentCodec() {
        this(
                new LocalDateAsDocumentCodec(),
                new LocalTimeAsDocumentCodec()
        );
    }

    /**
     * Creates a {@code LocalDateTimeAsDocumentCodec} using
     * the provided codecs.
     */
    public LocalDateTimeAsDocumentCodec(
            Codec<LocalDate> localDateCodec,
            Codec<LocalTime> localTimeCodec) {

        this.localDateCodec = localDateCodec;
        this.localTimeCodec = localTimeCodec;

        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("date", localDateCodec::decode);
        fd.put("time", localTimeCodec::decode);
        fieldDecoders = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            LocalDateTime value,
            EncoderContext encoderContext) {

        writer.writeStartDocument();
        writer.writeName("date");
        localDateCodec.encode(writer, value.toLocalDate(), encoderContext);
        writer.writeName("time");
        localTimeCodec.encode(writer, value.toLocalTime(), encoderContext);
        writer.writeEndDocument();
    }

    @Override
    public LocalDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, fieldDecoders),
                val -> of(
                        getFieldValue(val, "date", LocalDate.class),
                        getFieldValue(val, "time", LocalTime.class)
                )
        );
    }

    @Override
    public Class<LocalDateTime> getEncoderClass() {
        return LocalDateTime.class;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        LocalDateTimeAsDocumentCodec rhs = (LocalDateTimeAsDocumentCodec)obj;

        return localDateCodec.equals(rhs.localDateCodec) &&
               localTimeCodec.equals(rhs.localTimeCodec) &&
               fieldDecoders.equals(rhs.fieldDecoders);
    }

    @Override
    public int hashCode() {
        int result = localDateCodec.hashCode();
        result = 31 * result + localTimeCodec.hashCode();
        result = 31 * result + fieldDecoders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LocalDateTimeAsDocumentCodec[" +
               "localDateCodec=" + localDateCodec +
               ",localTimeCodec=" + localTimeCodec +
               ",fieldDecoders=" + fieldDecoders +
               ']';
    }
}
