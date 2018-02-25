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

import static io.github.cbartosiak.bson.codecs.jsr310.internal.DocumentCodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.DocumentCodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.DocumentCodecsUtil.writeDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.ExceptionsUtil.translateDecodeExceptions;
import static java.time.OffsetTime.of;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDocumentCodec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code OffsetTime} values to and from
 * {@code BSON Document}, such as:
 * <pre>
 * {
 *     time: { hour: 10, minute: 15, second: 30, nano: 0 },
 *     offset: 3600
 * }
 * </pre>
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code time} (a non-null {@code Document}):
 * <ul>
 * <li>{@code hour} (a non-null {@code Int32});
 * <li>{@code minute} (a non-null {@code Int32});
 * <li>{@code second} (a non-null {@code Int32});
 * <li>{@code nano} (a non-null {@code Int32});
 * </ul>
 * <li>{@code offset} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class OffsetTimeAsDocumentCodec
        implements Codec<OffsetTime> {

    @Override
    public void encode(
            BsonWriter writer,
            OffsetTime value,
            EncoderContext encoderContext) {

        writeDocument(
                writer,
                new Document()
                        .append(
                                "time",
                                LocalTimeAsDocumentCodec.toDocument(
                                        value.toLocalTime()
                                )
                        )
                        .append("offset", value.getOffset().getTotalSeconds()),
                encoderContext
        );
    }

    @Override
    public OffsetTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext),
                val -> of(
                        LocalTimeAsDocumentCodec.fromDocument(
                                getFieldValue(val, "time", Document.class)
                        ),
                        ZoneOffset.ofTotalSeconds(
                                getFieldValue(val, "offset", Integer.class)
                        )
                )
        );
    }

    @Override
    public Class<OffsetTime> getEncoderClass() {
        return OffsetTime.class;
    }
}
