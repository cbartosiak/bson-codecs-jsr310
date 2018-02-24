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

import static io.github.cbartosiak.bson.codecs.jsr310.DocumentCodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.DocumentCodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.DocumentCodecsUtil.writeDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.ExceptionsUtil.translateDecodeExceptions;
import static java.time.OffsetDateTime.of;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import io.github.cbartosiak.bson.codecs.jsr310.localdatetime.LocalDateTimeAsDocumentCodec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code OffsetDateTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     dateTime: {
 *         date: { year: 2018, month: 1, day: 2 },
 *         time: { hour: 10, minute: 15, second: 30, nano: 0 }
 *     },
 *     offset: 3600
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code dateTime} (a non-null {@code Document}):
 * <ul>
 * <li>{@code date} (a non-null {@code Document}):
 * <ul>
 * <li>{@code year} (a non-null {@code Int32});
 * <li>{@code month} (a non-null {@code Int32});
 * <li>{@code day} (a non-null {@code Int32});
 * </ul>
 * <li>{@code time} (a non-null {@code Document}):
 * <ul>
 * <li>{@code hour} (a non-null {@code Int32});
 * <li>{@code minute} (a non-null {@code Int32});
 * <li>{@code second} (a non-null {@code Int32});
 * <li>{@code nano} (a non-null {@code Int32});
 * </ul>
 * </ul>
 * <li>{@code offset} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class OffsetDateTimeAsDocumentCodec
        implements Codec<OffsetDateTime> {

    @Override
    public void encode(
            BsonWriter writer,
            OffsetDateTime value,
            EncoderContext encoderContext) {

        writeDocument(
                writer,
                new Document()
                        .append(
                                "dateTime",
                                LocalDateTimeAsDocumentCodec.toDocument(
                                        value.toLocalDateTime()
                                )
                        )
                        .append("offset", value.getOffset().getTotalSeconds()),
                encoderContext
        );
    }

    @Override
    public OffsetDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext),
                val -> of(
                        LocalDateTimeAsDocumentCodec.fromDocument(
                                getFieldValue(val, "dateTime", Document.class)
                        ),
                        ZoneOffset.ofTotalSeconds(
                                getFieldValue(val, "offset", Integer.class)
                        )
                )
        );
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
