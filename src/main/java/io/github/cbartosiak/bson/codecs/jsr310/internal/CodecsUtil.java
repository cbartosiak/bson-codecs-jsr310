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

package io.github.cbartosiak.bson.codecs.jsr310.internal;

import static java.lang.String.format;
import static org.bson.BsonType.END_OF_DOCUMENT;

import java.time.DateTimeException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.Document;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;

public final class CodecsUtil {

    private CodecsUtil() {}

    // Exceptions

    public static <Value> void translateEncodeExceptions(
            Supplier<Value> valueSupplier,
            Consumer<Value> valueConsumer) {

        Value value = valueSupplier.get();
        try {
            valueConsumer.accept(value);
        }
        catch (ArithmeticException |
                DateTimeException |
                NumberFormatException ex) {

            throw new BsonInvalidOperationException(format(
                    "The value %s is not supported", value
            ), ex);
        }
    }

    public static <Value, Result> Result translateDecodeExceptions(
            Supplier<Value> valueSupplier,
            Function<Value, Result> valueConverter) {

        Value value = valueSupplier.get();
        try {
            return valueConverter.apply(value);
        }
        catch (ArithmeticException |
                DateTimeException |
                IllegalArgumentException ex) {

            throw new BsonInvalidOperationException(format(
                    "The value %s is not supported", value
            ), ex);
        }
    }

    // Document codecs

    public static Document readDocument(
            BsonReader reader,
            DecoderContext decoderContext,
            Map<String, Decoder<?>> fieldDecoders) {

        Document document = new Document();
        reader.readStartDocument();
        while (reader.readBsonType() != END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            if (fieldDecoders.containsKey(fieldName)) {
                document.put(
                        fieldName,
                        fieldDecoders
                                .get(fieldName)
                                .decode(reader, decoderContext)
                );
            }
            else {
                throw new BsonInvalidOperationException(format(
                        "The field %s is not expected here", fieldName
                ));
            }
        }
        reader.readEndDocument();
        return document;
    }

    public static <Value> Value getFieldValue(
            Document document,
            Object key,
            Class<Value> clazz) {

        try {
            Value value = document.get(key, clazz);
            if (value == null) {
                throw new BsonInvalidOperationException(format(
                        "The value of the field %s is null", key
                ));
            }
            return value;
        }
        catch (ClassCastException ex) {
            throw new BsonInvalidOperationException(format(
                    "The value of the field %s is not of the type %s",
                    key, clazz.getName()
            ), ex);
        }
    }
}
