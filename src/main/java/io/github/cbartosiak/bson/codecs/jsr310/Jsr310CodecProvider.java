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

import java.util.HashMap;
import java.util.Map;

import io.github.cbartosiak.bson.codecs.jsr310.duration.DurationAsDecimal128Codec;
import io.github.cbartosiak.bson.codecs.jsr310.instant.InstantAsDateTimeCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localdate.LocalDateAsDateTimeCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localdatetime.LocalDateTimeAsDateTimeCodec;
import io.github.cbartosiak.bson.codecs.jsr310.localtime.LocalTimeAsDateTimeCodec;
import io.github.cbartosiak.bson.codecs.jsr310.monthday.MonthDayAsDecimal128Codec;
import io.github.cbartosiak.bson.codecs.jsr310.offsetdatetime.OffsetDateTimeAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.offsettime.OffsetTimeAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.period.PeriodAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.year.YearAsInt32Codec;
import io.github.cbartosiak.bson.codecs.jsr310.yearmonth.YearMonthAsDecimal128Codec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneddatetime.ZonedDateTimeAsStringCodec;
import io.github.cbartosiak.bson.codecs.jsr310.zoneoffset.ZoneOffsetAsInt32Codec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * <p>
 * Provides the following JSR-310 related codecs:
 * <ul>
 * <li>{@link DurationAsDecimal128Codec}
 * <li>{@link InstantAsDateTimeCodec}
 * <li>{@link LocalDateAsDateTimeCodec}
 * <li>{@link LocalDateTimeAsDateTimeCodec}
 * <li>{@link LocalTimeAsDateTimeCodec}
 * <li>{@link MonthDayAsDecimal128Codec}
 * <li>{@link OffsetDateTimeAsStringCodec}
 * <li>{@link OffsetTimeAsStringCodec}
 * <li>{@link PeriodAsStringCodec}
 * <li>{@link YearAsInt32Codec}
 * <li>{@link YearMonthAsDecimal128Codec}
 * <li>{@link ZonedDateTimeAsStringCodec}
 * <li>{@link ZoneOffsetAsInt32Codec}
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class Jsr310CodecProvider
        implements CodecProvider {

    private static final Map<Class<?>, Codec<?>> CODECS = new HashMap<>();

    static {
        putCodec(new DurationAsDecimal128Codec());
        putCodec(new InstantAsDateTimeCodec());
        putCodec(new LocalDateAsDateTimeCodec());
        putCodec(new LocalDateTimeAsDateTimeCodec());
        putCodec(new LocalTimeAsDateTimeCodec());
        putCodec(new MonthDayAsDecimal128Codec());
        putCodec(new OffsetDateTimeAsStringCodec());
        putCodec(new OffsetTimeAsStringCodec());
        putCodec(new PeriodAsStringCodec());
        putCodec(new YearAsInt32Codec());
        putCodec(new YearMonthAsDecimal128Codec());
        putCodec(new ZonedDateTimeAsStringCodec());
        putCodec(new ZoneOffsetAsInt32Codec());
    }

    private static void putCodec(Codec<?> codec) {
        CODECS.put(codec.getEncoderClass(), codec);
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        //noinspection unchecked
        return (Codec<T>)CODECS.get(clazz);
    }
}
