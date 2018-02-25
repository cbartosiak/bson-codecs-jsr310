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

package io.github.cbartosiak.bson.codecs.jsr310.instant;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.DocumentCodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.DocumentCodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.DocumentCodecsUtil.writeDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.ExceptionsUtil.translateDecodeExceptions;
import static java.time.Instant.ofEpochSecond;

import java.time.Instant;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code Instant} values to and from
 * {@code BSON Document}, such as
 * {@code { seconds: 1514888130, nanos: 0 }}.
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code seconds} (a non-null {@code Int64});
 * <li>{@code nanos} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class InstantAsDocumentCodec
        implements Codec<Instant> {

    @Override
    public void encode(
            BsonWriter writer,
            Instant value,
            EncoderContext encoderContext) {

        writeDocument(
                writer,
                new Document()
                        .append("seconds", value.getEpochSecond())
                        .append("nanos", value.getNano()),
                encoderContext
        );
    }

    @Override
    public Instant decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext),
                val -> ofEpochSecond(
                        getFieldValue(val, "seconds", Long.class),
                        getFieldValue(val, "nanos", Integer.class)
                )
        );
    }

    @Override
    public Class<Instant> getEncoderClass() {
        return Instant.class;
    }
}
