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

    private static <T> void testCodec(Codec<T> codec, T value) {
        try (BasicOutputBuffer output = new BasicOutputBuffer()) {

            encode(output, codec, value);

            T decoded = decode(wrap(output.toByteArray()), codec);

            assertEquals(value, decoded);
        }
    }

    private static <T> void encode(BsonOutput output, Codec<T> codec, T value) {
        try (BsonBinaryWriter writer = new BsonBinaryWriter(output)) {

            writer.writeStartDocument();
            writer.writeName("value");

            codec.encode(writer, value, EncoderContext.builder().build());

            writer.writeEndDocument();
            writer.close();
        }
    }

    private static <T> T decode(ByteBuffer byteBuffer, Codec<T> codec) {
        try (BsonBinaryReader reader = new BsonBinaryReader(byteBuffer)) {

            reader.readStartDocument();

            String name = reader.readName();
            assertEquals("value", name);

            return codec.decode(reader, DecoderContext.builder().build());
        }
    }

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
}
