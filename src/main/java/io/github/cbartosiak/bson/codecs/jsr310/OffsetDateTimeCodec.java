/*
 * MIT License
 *
 * Copyright (c) 2017 Cezary Bartosiak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.cbartosiak.bson.codecs.jsr310;

import static java.time.OffsetDateTime.parse;

import java.time.OffsetDateTime;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * <p>
 * Encodes and decodes {@code OffsetDateTime} objects to and from
 * {@code String}, such as {@code 2007-12-03T10:15:30+01:00}.
 * <p>
 * Objects are stored in ISO-8601 formats, see
 * {@link OffsetDateTime#toString()}.
 * <p>
 * The implementation is <b>thread-safe</b>.
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/bson-types/">
 * BSON Types
 * </a>
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

        return parse(reader.readString());
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
