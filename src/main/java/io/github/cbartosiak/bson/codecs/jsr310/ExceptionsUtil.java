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

import java.time.DateTimeException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bson.BsonInvalidOperationException;

/**
 * <p>
 * Contains translators for exceptions thrown during encoding and decoding.
 * <p>
 * This type is a <b>utilities</b> container and cannot be instantiated.
 */
public final class ExceptionsUtil {

    private ExceptionsUtil() {}

    /**
     * <p>
     * Translates exceptions thrown during encoding into
     * {@code BsonInvalidOperationException}. The value supplier is used to get
     * a value to encode by the value consumer.
     * <p>
     * The following exceptions are translated:
     * <ul>
     * <li>{@code NumberFormatException};
     * <li>{@code ArithmeticException};
     * <li>{@code DateTimeException}.
     * </ul>
     *
     * @param valueSupplier not null
     * @param valueConsumer not null
     * @param <Value>       the value type
     */
    public static <Value> void translateEncodeExceptions(
            Supplier<Value> valueSupplier,
            Consumer<Value> valueConsumer) {

        Value value = valueSupplier.get();
        try {
            valueConsumer.accept(value);
        }
        catch (NumberFormatException |
                ArithmeticException |
                DateTimeException ex) {

            throw new BsonInvalidOperationException(
                    format(
                            "The value %s is not supported",
                            value
                    ),
                    ex
            );
        }
    }

    /**
     * <p>
     * Translates exceptions thrown during decoding into
     * {@code BsonInvalidOperationException}. The value supplier is used to get
     * a value to decode by the value converter.
     * <p>
     * The following exceptions are translated:
     * <ul>
     * <li>{@code ArithmeticException};
     * <li>{@code DateTimeException}.
     * </ul>
     *
     * @param valueSupplier  not null
     * @param valueConverter not null
     * @param <Value>        the value type
     * @param <Result>       the result type
     *
     * @return the value converter {@code Result}
     */
    public static <Value, Result> Result translateDecodeExceptions(
            Supplier<Value> valueSupplier,
            Function<Value, Result> valueConverter) {

        Value value = valueSupplier.get();
        try {
            return valueConverter.apply(value);
        }
        catch (ArithmeticException |
                DateTimeException ex) {

            throw new BsonInvalidOperationException(
                    format(
                            "The value %s is not supported",
                            value
                    ),
                    ex
            );
        }
    }
}
