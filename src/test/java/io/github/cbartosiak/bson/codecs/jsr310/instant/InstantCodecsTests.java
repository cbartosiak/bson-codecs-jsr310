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

package io.github.cbartosiak.bson.codecs.jsr310.instant;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import io.github.cbartosiak.bson.codecs.jsr310.AbstractCodecsTests;
import org.bson.BsonInvalidOperationException;
import org.junit.jupiter.api.Test;

final class InstantCodecsTests
        extends AbstractCodecsTests {

    private InstantCodecsTests() {}

    @Test
    void testInstantAsDateTimeCodec() {
        InstantAsDateTimeCodec instantAsDateTimeCodec =
                new InstantAsDateTimeCodec();
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(instantAsDateTimeCodec, Instant.MIN)
        );
        assertThrows(
                BsonInvalidOperationException.class,
                () -> testCodec(instantAsDateTimeCodec, Instant.MAX)
        );
        testCodec(instantAsDateTimeCodec, Instant.EPOCH);
        testCodec(instantAsDateTimeCodec, Instant.now());
    }
}
