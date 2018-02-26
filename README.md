## BSON codecs for Java 8 Date and Time API (JSR-310)

[![Build Status](https://travis-ci.org/cbartosiak/bson-codecs-jsr310.svg?branch=dev)](https://travis-ci.org/cbartosiak/bson-codecs-jsr310)
[![Javadocs](https://www.javadoc.io/badge/io.github.cbartosiak/bson-codecs-jsr310.svg?color=blue)](https://www.javadoc.io/doc/io.github.cbartosiak/bson-codecs-jsr310)

The library provides codecs for the following JSR-310 classes:
* `java.time.DayOfWeek`
* `java.time.Duration`
* `java.time.Instant`
* `java.time.LocalDate`
* `java.time.LocalDateTime`
* `java.time.LocalTime`
* `java.time.Month`
* `java.time.MonthDay`
* `java.time.OffsetDateTime`
* `java.time.OffsetTime`
* `java.time.Period`
* `java.time.Year`
* `java.time.YearMonth`
* `java.time.ZonedDateTime`
* `java.time.ZoneId`
* `java.time.ZoneOffset`

### Usage

It is available in Maven Central Repository:
```
<dependency>
    <groupId>io.github.cbartosiak</groupId>
    <artifactId>bson-codecs-jsr310</artifactId>
    <version>3.0.1</version>
</dependency>
```

In order to utilize the codecs one can use `CodecRegistries` helper, for example
in case of Mongo synchronous client:
```
MongoClient client = ...
```
```
CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
        MongoClient.getDefaultCodecRegistry(),
        CodecRegistries.fromCodecs(new DurationAsDecimal128Codec())
);
```
```
MongoDatabase database = client
        .getDatabase(...)
        .withCodecRegistry(codecRegistry);
```

Note that depending on a context a different set of codecs might be necessary.
There are three main factors to consider when choosing codecs:
* **queryability** - how much stored values are capable of being searched;
* **sortability** - how much stored values are capable of being ordered;
* **readability** - how much stored values are readable for human.

The table below presents the recommendations for all the factors. The more **+**
signs a cell contains the better a row codec is in terms of a column factor:

| Codec                           | Queryability | Sortability | Readability |
| :----                           | :----------: | :---------: | :---------: |
| `DayOfWeekAsInt32Codec`         | ++           | ++          | +           |
| `DayOfWeekAsStringCodec`        | +            |             | ++          |
| `DurationAsDecimal128Codec`     | +            | ++          |             |
| `DurationAsDocumentCodec`       | ++           | +           | +           |
| `DurationAsStringCodec`         |              |             | ++          |
| `InstantAsDateTimeCodec`        | +            | ++          | +           |
| `InstantAsDocumentCodec`        | ++           | +           | +           |
| `InstantAsStringCodec`          |              |             | ++          |
| `LocalDateAsDateTimeCodec`      | +            | ++          | +           |
| `LocalDateAsDocumentCodec`      | ++           | +           | +           |
| `LocalDateAsStringCodec`        |              |             | ++          |
| `LocalDateTimeAsDateTimeCodec`  | +            | ++          | +           |
| `LocalDateTimeAsDocumentCodec`  | ++           | +           | +           |
| `LocalDateTimeAsStringCodec`    |              |             | ++          |
| `LocalTimeAsDateTimeCodec`      | +            | ++          | +           |
| `LocalTimeAsDocumentCodec`      | ++           | +           | +           |
| `LocalTimeAsInt64Codec`         | ++           | ++          |             |
| `LocalTimeAsStringCodec`        |              |             | ++          |
| `MonthAsInt32Codec`             | ++           | ++          | +           |
| `MonthAsStringCodec`            | +            |             | ++          |
| `MonthDayAsDecimal128Codec`     | +            | ++          | ++          |
| `MonthDayAsDocumentCodec`       | ++           | +           | +           |
| `MonthDayAsStringCodec`         |              |             | ++          |
| `OffsetDateTimeAsDocumentCodec` | ++           | +           | +           |
| `OffsetDateTimeAsStringCodec`   |              |             | ++          |
| `OffsetTimeAsDocumentCodec`     | ++           | +           | +           |
| `OffsetTimeAsStringCodec`       |              |             | ++          |
| `PeriodAsDocumentCodec`         | +            |             | +           |
| `PeriodAsStringCodec`           |              |             | ++          |
| `YearAsInt32Codec`              | ++           | ++          | ++          |
| `YearMonthAsDecimal128Codec`    | +            | ++          | ++          |
| `YearMonthAsDocumentCodec`      | ++           | +           | +           |
| `YearMonthAsStringCodec`        |              |             | ++          |
| `ZonedDateTimeAsDocumentCodec`  | ++           | +           | +           |
| `ZonedDateTimeAsStringCodec`    |              |             | ++          |
| `ZoneIdAsStringCodec`           | +            |             | ++          |
| `ZoneOffsetAsInt32Codec`        | ++           | ++          |             |
| `ZoneOffsetAsStringCodec`       | +            |             | ++          |
