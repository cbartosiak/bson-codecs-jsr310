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

import static java.nio.ByteBuffer.wrap;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.BsonOutput;
import org.junit.jupiter.api.Test;

final class CodecsTests {

    private CodecsTests() {}

    @Test
    void testDurationCodec() {
        testCodec(new DurationCodec(), Duration.ofHours(12));
    }

    @Test
    void testInstantCodec() {
        testCodec(new InstantCodec(), Instant.now());
    }

    @Test
    void testLocalDateCodec() {
        testCodec(new LocalDateCodec(), LocalDate.now());
    }

    @Test
    void testLocalDateTimeCodec() {
        testCodec(new LocalDateTimeCodec(), LocalDateTime.now());
    }

    @Test
    void testLocalTimeCodec() {
        testCodec(new LocalTimeCodec(), LocalTime.now());
    }

    @Test
    void testMonthDayCodec() {
        testCodec(new MonthDayCodec(), MonthDay.now());
    }

    @Test
    void testOffsetDateTimeCodec() {
        testCodec(new OffsetDateTimeCodec(), OffsetDateTime.now());
    }

    @Test
    void testOffsetTimeCodec() {
        testCodec(new OffsetTimeCodec(), OffsetTime.now());
    }

    @Test
    void testPeriodCodec() {
        testCodec(new PeriodCodec(), Period.of(1, 6, 15));
    }

    @Test
    void testYearCodec() {
        testCodec(new YearCodec(), Year.now());
    }

    @Test
    void testYearMonthCodec() {
        testCodec(new YearMonthCodec(), YearMonth.now());
    }

    @Test
    void testZonedDateTimeCodec() {
        testCodec(new ZonedDateTimeCodec(), ZonedDateTime.now());
    }

    @Test
    void testZoneOffsetCodec() {
        testCodec(new ZoneOffsetCodec(), UTC);
    }

    private <T> void testCodec(Codec<T> codec, T value) {
        try (BasicOutputBuffer output = new BasicOutputBuffer()) {

            encodeValue(output, codec, value);

            T decoded = decodeValue(
                    wrap(output.toByteArray()),
                    codec
            );

            assertEquals(value, decoded);
        }
    }

    private <T> void encodeValue(BsonOutput output, Codec<T> codec, T value) {
        try (BsonBinaryWriter writer = new BsonBinaryWriter(output)) {

            writer.writeStartDocument();
            writer.writeName("value");

            codec.encode(writer, value, EncoderContext.builder().build());

            writer.writeEndDocument();
            writer.close();
        }
    }

    private <T> T decodeValue(ByteBuffer byteBuffer, Codec<T> codec) {
        try (BsonBinaryReader reader = new BsonBinaryReader(byteBuffer)) {

            reader.readStartDocument();

            String name = reader.readName();
            assertEquals("value", name);

            return codec.decode(reader, DecoderContext.builder().build());
        }
    }
}
