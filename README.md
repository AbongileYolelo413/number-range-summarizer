# Number Range Summarizer

Turns a comma separated list of numbers into a summary where sequential numbers are grouped into ranges:

```
Input:  1,3,6,7,8,12,13,14,15,21,22,23,24,31
Output: 1, 3, 6-8, 12-15, 21-24, 31
```

Java 8+, Maven, JUnit 4. Run the tests with `mvn test`.

## Design

`RangeSummarizer` implements the provided `NumberRangeSummarizer` interface and delegates to three
single-purpose collaborators (injected through the constructor, so each can be swapped or tested alone):

| Class | Responsibility |
|---|---|
| `NumberListParser` | text -> numbers (`"1, 2,3"` -> `[1, 2, 3]`) |
| `RangeGrouper` | numbers -> `NumberRange`s (sorts, de-duplicates, merges consecutive values) |
| `RangeFormatter` | `NumberRange`s -> text (`"1, 3, 6-8"`) |
| `NumberRange` | immutable value object for an inclusive run of integers |

Java 8 features used: streams, `Collectors.joining`, lambdas and method references, `Objects::nonNull`.

## Assumptions

Each assumption is pinned down by a test (see the `assumption_*` tests in `RangeSummarizerTest`).

1. Output is sorted ascending, whatever the input order.
2. Duplicates are collapsed (`1,1,2` is treated as `1,2`).
3. "Sequential" means consecutive integers (each exactly 1 more than the previous).
4. A range needs at least 3 numbers: `1,2` stays `1, 2`; `1,2,3` becomes `1-3`. Configurable via `new RangeFormatter(2)`.
5. Negative numbers and zero are valid.
6. Whitespace is ignored, and empty entries (`1,,2` or a trailing comma) are skipped.
7. Null or blank input gives an empty collection / empty string, not an error.
8. Null elements inside a collection are ignored.
9. A non-integer entry (`abc`, `2.5`, or a number too big for `int`) throws `IllegalArgumentException`.
10. `collect()` only parses (keeps input order and duplicates); normalising happens in `summarizeCollection()`.
