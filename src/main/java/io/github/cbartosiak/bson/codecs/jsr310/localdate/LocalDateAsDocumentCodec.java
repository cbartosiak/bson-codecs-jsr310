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

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.writeDocument;
import static java.time.LocalDate.of;

import java.time.LocalDate;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code LocalDate} values to and from
 * {@code BSON Document}, such as
 * {@code { year: 2018, month: 1, day: 2 }}.
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code year} (a non-null {@code Int32});
 * <li>{@code month} (a non-null {@code Int32});
 * <li>{@code day} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class LocalDateAsDocumentCodec
        implements Codec<LocalDate> {

    /**
     * Converts the local date to a document.
     *
     * @param localDate not null
     *
     * @return a non-null {@code Document}
     */
    public static Document toDocument(LocalDate localDate) {
        return new Document()
                .append("year", localDate.getYear())
                .append("month", localDate.getMonthValue())
                .append("day", localDate.getDayOfMonth());
    }

    /**
     * Converts the document to a local date.
     *
     * @param document not null
     *
     * @return a non-null {@code LocalDate}
     */
    public static LocalDate fromDocument(Document document) {
        return of(
                getFieldValue(document, "year", Integer.class),
                getFieldValue(document, "month", Integer.class),
                getFieldValue(document, "day", Integer.class)
        );
    }

    @Override
    public void encode(
            BsonWriter writer,
            LocalDate value,
            EncoderContext encoderContext) {

        writeDocument(writer, toDocument(value), encoderContext);
    }

    @Override
    public LocalDate decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext),
                LocalDateAsDocumentCodec::fromDocument
        );
    }

    @Override
    public Class<LocalDate> getEncoderClass() {
        return LocalDate.class;
    }
}
