## BSON codecs for Java 8 Date and Time API (JSR-310)

[![Build Status](https://travis-ci.org/cbartosiak/bson-codecs-jsr310.svg?branch=master)](https://travis-ci.org/cbartosiak/bson-codecs-jsr310)

This library provides codecs for the following JSR-310 classes:
* `java.time.Duration`
* `java.time.Instant`
* `java.time.LocalDate`
* `java.time.LocalDateTime`
* `java.time.LocalTime`
* `java.time.MonthDay`
* `java.time.OffsetDateTime`
* `java.time.OffsetTime`
* `java.time.Period`
* `java.time.Year`
* `java.time.YearMonth`
* `java.time.ZonedDateTime`
* `java.time.ZoneOffset`

### Usage

The library will be soon available in _Maven_.

To utilize the provided codecs one can use `Jsr310CodecProvider`, for example
in case of the synchronous client:

```
MongoClient client = ...
```
```
CodecRegistry codecRegistry = CodecRegistries.fromProviders(
        MongoClient.getDefaultCodecRegistry(),
        new Jsr310CodecProvider()
);
```
```
Mongo Database database = client
        .getDatabase(...)
        .withCodecRegistry(codecRegistry);
```

Be also aware of the fact that due to the limitations of BSON date type the
following codecs encode values as `Strings` and as such **do not support**
sorting:
* `OffsetDateTimeCodec`
* `OffsetTimeCodec`
* `ZonedDateTimeCodec`

Additionally `java.time.Period` values are not comparable by design.

All other codecs support sorting.
