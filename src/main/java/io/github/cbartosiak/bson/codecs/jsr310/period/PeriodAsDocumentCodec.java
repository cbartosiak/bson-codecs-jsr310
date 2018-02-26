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

package io.github.cbartosiak.bson.codecs.jsr310.period;

import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.getFieldValue;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.readDocument;
import static io.github.cbartosiak.bson.codecs.jsr310.internal.CodecsUtil.translateDecodeExceptions;
import static java.time.Period.of;
import static java.util.Collections.unmodifiableMap;

import java.time.Period;
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
 * Encodes and decodes {@code Period} values to and from
 * {@code BSON Document}, such as
 * {@code { years: 18, months: 1, days: 2 }}.
 * <p>
 * The values are stored using the following structure:
 * <ul>
 * <li>{@code years} (a non-null {@code Int32});
 * <li>{@code months} (a non-null {@code Int32});
 * <li>{@code days} (a non-null {@code Int32}).
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class PeriodAsDocumentCodec
        implements Codec<Period> {

    private static final Map<String, Decoder<?>> FIELD_DECODERS;

    static {
        Map<String, Decoder<?>> fd = new HashMap<>();
        fd.put("years", (r, dc) -> r.readInt32());
        fd.put("months", (r, dc) -> r.readInt32());
        fd.put("days", (r, dc) -> r.readInt32());
        FIELD_DECODERS = unmodifiableMap(fd);
    }

    @Override
    public void encode(
            BsonWriter writer,
            Period value,
            EncoderContext encoderContext) {

        writer.writeStartDocument();
        writer.writeInt32("years", value.getYears());
        writer.writeInt32("months", value.getMonths());
        writer.writeInt32("days", value.getDays());
        writer.writeEndDocument();
    }

    @Override
    public Period decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return translateDecodeExceptions(
                () -> readDocument(reader, decoderContext, FIELD_DECODERS),
                val -> of(
                        getFieldValue(val, "years", Integer.class),
                        getFieldValue(val, "months", Integer.class),
                        getFieldValue(val, "days", Integer.class)
                )
        );
    }

    @Override
    public Class<Period> getEncoderClass() {
        return Period.class;
    }
}
