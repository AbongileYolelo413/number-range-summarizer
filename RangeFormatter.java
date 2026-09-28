package numberrangesummarizer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders ranges as text, e.g. [1], [3], [6-8] as "1, 3, 6-8".
 * A range is only written as "start-end" when it holds at least {@code minRangeLength}
 * numbers; shorter runs are written as individual numbers.
 */
public class RangeFormatter {

    public static final int DEFAULT_MIN_RANGE_LENGTH = 3;
    private static final String ITEM_DELIMITER = ", ";
    private static final String RANGE_SEPARATOR = "-";

    private final int minRangeLength;

    public RangeFormatter() {
        this(DEFAULT_MIN_RANGE_LENGTH);
    }

    /** @param minRangeLength must be at least 2 (a "range" of one number is just a number) */
    public RangeFormatter(int minRangeLength) {
        if (minRangeLength < 2) {
            throw new IllegalArgumentException("minRangeLength must be at least 2 but was " + minRangeLength);
        }
        this.minRangeLength = minRangeLength;
    }

    public String format(List<NumberRange> ranges) {
        if (ranges == null) {
            ranges = Collections.emptyList();
        }
        return ranges.stream()
                .flatMap(range -> toItems(range).stream())
                .collect(Collectors.joining(ITEM_DELIMITER));
    }

    private List<String> toItems(NumberRange range) {
        if (range.size() >= minRangeLength) {
            return Collections.singletonList(range.getStart() + RANGE_SEPARATOR + range.getEnd());
        }
        return range.numbers().stream().map(String::valueOf).collect(Collectors.toList());
    }
}
