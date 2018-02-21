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

import java.util.HashMap;
import java.util.Map;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * <p>
 * Provides JSR-310 codecs:
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

    private final Map<Class<?>, Codec<?>> codecs = new HashMap<>();

    /**
     * Creates a {@code Jsr310CodecProvider}.
     */
    @SuppressWarnings("OverlyCoupledMethod")
    public Jsr310CodecProvider() {
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        return (Codec<T>)codecs.get(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }

        Jsr310CodecProvider rhs = (Jsr310CodecProvider)obj;

        return codecs.equals(rhs.codecs);
    }

    @Override
    public int hashCode() {
        return codecs.hashCode();
    }

    @Override
    public String toString() {
        return format("Jsr310CodecProvider[codecs=%s]", codecs);
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    private <T> void putCodec(Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }
}
