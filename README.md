# Number Range Summarizer

Turns `1,3,6,7,8,12,13,14,15,21,22,23,24,31` into `1, 3, 6-8, 12-15, 21-24, 31`.

- Java 8+, Maven, JUnit 4
- `numberrangesummarizer.NumberRangeSummarizer` is the provided interface; `RangeSummarizer` implements it
- Output is sorted and de-duplicated; runs of 3+ consecutive numbers become ranges (a pair like `1,2` stays as-is)
- Invalid (non-integer) input throws `IllegalArgumentException`

Run tests: `mvn test`
