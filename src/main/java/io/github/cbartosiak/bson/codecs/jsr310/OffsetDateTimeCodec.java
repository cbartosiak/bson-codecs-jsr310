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

package io.github.cbartosiak.bson.codecs.jsr310;

import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code OffsetDateTime} values to and from
 * {@code BSON String}, such as {@code 2007-12-03T10:15:30+01:00}.
 * <p>
 * Values are stored in ISO-8601 formats,
 * see {@link OffsetDateTime#toString()}.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class OffsetDateTimeCodec
        implements Codec<OffsetDateTime> {

    @Override
    public void encode(
            BsonWriter writer,
            OffsetDateTime value,
            EncoderContext encoderContext) {

        writer.writeString(value.toString());
    }

    @Override
    public OffsetDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        String str = reader.readString();
        try {
            return parse(str);
        }
        catch (DateTimeParseException ex) {
            throw new BsonInvalidOperationException(format(
                    "The value %s is not supported", str
            ), ex);
        }
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
