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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.BsonInvalidOperationException;
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
        DurationCodec durationCodec = new DurationCodec();
        testCodec(durationCodec, Duration.ZERO);
        testCodec(durationCodec, Duration.ofSeconds(
                Long.MAX_VALUE, 999_999_999L
        ));
        testCodec(durationCodec, Duration.ofHours(12));
    }

    @Test
    void testInstantCodec() {
        InstantCodec instantCodec = new InstantCodec();
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(instantCodec, Instant.MIN)
        );
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(instantCodec, Instant.MAX)
        );
        testCodec(instantCodec, Instant.EPOCH);
        testCodec(instantCodec, Instant.now());
    }

    @Test
    void testLocalDateCodec() {
        LocalDateCodec localDateCodec = new LocalDateCodec();
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(localDateCodec, LocalDate.MIN)
        );
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(localDateCodec, LocalDate.MAX)
        );
        testCodec(localDateCodec, LocalDate.now());
    }

    @Test
    void testLocalDateTimeCodec() {
        LocalDateTimeCodec localDateTimeCodec = new LocalDateTimeCodec();
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(localDateTimeCodec, LocalDateTime.MIN)
        );
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(localDateTimeCodec, LocalDateTime.MAX)
        );
        testCodec(localDateTimeCodec, LocalDateTime.now());
    }

    @Test
    void testLocalTimeCodec() {
        LocalTimeCodec localTimeCodec = new LocalTimeCodec();
        testCodec(localTimeCodec, LocalTime.MIN);
        testCodec(localTimeCodec, LocalTime.MAX.minusNanos(999999));
        testCodec(localTimeCodec, LocalTime.MIDNIGHT);
        testCodec(localTimeCodec, LocalTime.NOON);
        testCodec(localTimeCodec, LocalTime.now());
    }

    @Test
    void testMonthDayCodec() {
        MonthDayCodec monthDayCodec = new MonthDayCodec();
        testCodec(monthDayCodec, MonthDay.of(1, 1));
        testCodec(monthDayCodec, MonthDay.of(12, 31));
        testCodec(monthDayCodec, MonthDay.now());
    }

    @Test
    void testOffsetDateTimeCodec() {
        OffsetDateTimeCodec offsetDateTimeCodec = new OffsetDateTimeCodec();
        testCodec(offsetDateTimeCodec, OffsetDateTime.MIN);
        testCodec(offsetDateTimeCodec, OffsetDateTime.MAX);
        testCodec(offsetDateTimeCodec, OffsetDateTime.now());
    }

    @Test
    void testOffsetTimeCodec() {
        OffsetTimeCodec offsetTimeCodec = new OffsetTimeCodec();
        testCodec(offsetTimeCodec, OffsetTime.MIN);
        testCodec(offsetTimeCodec, OffsetTime.MAX);
        testCodec(offsetTimeCodec, OffsetTime.now());
    }

    @Test
    void testPeriodCodec() {
        PeriodCodec periodCodec = new PeriodCodec();
        testCodec(periodCodec, Period.ZERO);
        testCodec(periodCodec, Period.of(
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
        ));
        testCodec(periodCodec, Period.of(1, 6, 15));
    }

    @Test
    void testYearCodec() {
        YearCodec yearCodec = new YearCodec();
        testCodec(yearCodec, Year.of(Year.MIN_VALUE));
        testCodec(yearCodec, Year.of(Year.MAX_VALUE));
        testCodec(yearCodec, Year.now());
    }

    @Test
    void testYearMonthCodec() {
        YearMonthCodec yearMonthCodec = new YearMonthCodec();
        testCodec(yearMonthCodec, YearMonth.of(Year.MIN_VALUE, 1));
        testCodec(yearMonthCodec, YearMonth.of(Year.MAX_VALUE, 12));
        testCodec(yearMonthCodec, YearMonth.now());
    }

    @Test
    void testZonedDateTimeCodec() {
        ZonedDateTimeCodec zonedDateTimeCodec = new ZonedDateTimeCodec();
        testCodec(zonedDateTimeCodec, ZonedDateTime.of(
                Year.MIN_VALUE, 1, 1, 0, 0, 0, 0, ZoneOffset.MIN
        ));
        testCodec(zonedDateTimeCodec, ZonedDateTime.of(
                Year.MAX_VALUE, 12, 31, 23, 59, 59, 999, ZoneOffset.MAX
        ));
        testCodec(zonedDateTimeCodec, ZonedDateTime.now());
    }

    @Test
    void testZoneOffsetCodec() {
        ZoneOffsetCodec zoneOffsetCodec = new ZoneOffsetCodec();
        testCodec(zoneOffsetCodec, ZoneOffset.MIN);
        testCodec(zoneOffsetCodec, ZoneOffset.MAX);
        testCodec(zoneOffsetCodec, ZoneOffset.UTC);
    }
}
