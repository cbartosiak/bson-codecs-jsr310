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

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * <p>
 * Provides the following JSR-310 based codecs:
 * <ul>
 * <li>{@link DurationCodec}
 * <li>{@link InstantCodec}
 * <li>{@link LocalDateCodec}
 * <li>{@link LocalDateTimeCodec}
 * <li>{@link LocalTimeCodec}
 * <li>{@link MonthDayCodec}
 * <li>{@link OffsetDateTimeCodec}
 * <li>{@link OffsetTimeCodec}
 * <li>{@link PeriodCodec}
 * <li>{@link YearCodec}
 * <li>{@link YearMonthCodec}
 * <li>{@link ZonedDateTimeCodec}
 * <li>{@link ZoneOffsetCodec}
 * </ul>
 * <p>
 * This type is <b>immutable</b>.
 */
public final class Jsr310CodecProvider
        implements CodecProvider {

    private static final Map<Class<?>, Codec<?>> CODECS = new HashMap<>();

    static {
        putCodec(new DurationCodec());
        putCodec(new InstantCodec());
        putCodec(new LocalDateCodec());
        putCodec(new LocalDateTimeCodec());
        putCodec(new LocalTimeCodec());
        putCodec(new MonthDayCodec());
        putCodec(new OffsetDateTimeCodec());
        putCodec(new OffsetTimeCodec());
        putCodec(new PeriodCodec());
        putCodec(new YearCodec());
        putCodec(new YearMonthCodec());
        putCodec(new ZonedDateTimeCodec());
        putCodec(new ZoneOffsetCodec());
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
