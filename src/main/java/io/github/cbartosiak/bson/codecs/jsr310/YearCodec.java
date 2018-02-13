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

import static java.time.Year.of;

import java.time.Year;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code Year} objects to and from {@code 32-bit integer}.
 * <p>
 * Objects are stored as ISO proleptic year values.
 * <p>
 * The implementation is <b>thread-safe</b>.
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">
 * BSON Types</a>
 */
public final class YearCodec
        implements Codec<Year> {

    @Override
    public void encode(
            BsonWriter writer,
            Year value,
            EncoderContext encoderContext) {

        writer.writeInt32(value.getValue());
    }

    @Override
    public Year decode(
            BsonReader reader,
            DecoderContext decoderContext) {

        return of(reader.readInt32());
    }

    @Override
    public Class<Year> getEncoderClass() {
        return Year.class;
    }
}
