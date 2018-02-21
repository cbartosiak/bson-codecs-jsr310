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

import java.time.ZonedDateTime;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code ZonedDateTime} values to and from
 * {@code BSON String}, such as {@code 2007-12-03T10:15:30+01:00[Europe/Paris]}.
 * <p>
 * Values are stored in <i>quasi</i> ISO-8601 format,
 * see {@link ZonedDateTime#toString()}.
 * <p>
 * This type is <b>immutable</b>.
 */
public final class ZonedDateTimeCodec
        implements Codec<ZonedDateTime> {

    @Override
    public void encode(
            BsonWriter writer,
            ZonedDateTime value,
            EncoderContext encoderContext) {

        writer.writeString(value.toString());
    }

    @Override
    public ZonedDateTime decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return ZonedDateTime.parse(reader.readString());
    }

    @Override
    public Class<ZonedDateTime> getEncoderClass() {
        return ZonedDateTime.class;
    }
}
