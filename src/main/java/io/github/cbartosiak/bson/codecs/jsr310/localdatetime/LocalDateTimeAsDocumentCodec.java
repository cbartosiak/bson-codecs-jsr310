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

import static io.github.cbartosiak.bson.codecs.jsr310.DocumentCodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.DocumentCodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.DocumentCodecsUtil.writeDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.ExceptionsUtil.translateDecodeExceptions;
import static java.time.LocalDateTime.of;

import java.time.LocalDateTime;

import io.github.cbartosiak.bson.codecs.jsr310.localdate.LocalDateAsDocumentCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDocumentCodec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code LocalDateTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     date: { year: 2018, month: 1, day: 2 },
 *     time: { hour: 10, minute: 15, second: 30, nano: 0 }
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
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
 * <li>{@code nano} (a non-null {@code Int32}).
 * </ul>
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class LocalDateTimeAsDocumentCodec
        implements Codec<LocalDateTime> {

    /**
     * Converts the local date time to a document.
     *
     * @param localDateTime not null
     *
     * @return a non-null {@code Document}
     */
    public static Document toDocument(LocalDateTime localDateTime) {
        return new Document()
                .append("date", LocalDateAsDocumentCodec.toDocument(
                        localDateTime.toLocalDate()
                ))
                .append("time", LocalTimeAsDocumentCodec.toDocument(
                        localDateTime.toLocalTime()
                ));
    }

    /**
     * Converts the document to a local date time.
     *
     * @param document not null
     *
     * @return a non-null {@code LocalDateTime}
     */
    public static LocalDateTime fromDocument(Document document) {
        return of(
                LocalDateAsDocumentCodec.fromDocument(
                        getFieldValue(document, "date", Document.class)
                ),
                LocalTimeAsDocumentCodec.fromDocument(
                        getFieldValue(document, "time", Document.class)
                )
        );
    }

    @Override
    public void encode(
            BsonWriter writer,
            LocalDateTime value,
            EncoderContext encoderContext) {

        writeDocument(writer, toDocument(value), encoderContext);
    }

    @Override
    public LocalDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext),
                LocalDateTimeAsDocumentCodec::fromDocument
        );
    }

    @Override
    public Class<LocalDateTime> getEncoderClass() {
        return LocalDateTime.class;
    }
}
