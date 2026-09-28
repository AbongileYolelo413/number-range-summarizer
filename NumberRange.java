package numberrangesummarizer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Immutable value object representing an inclusive run of consecutive integers,
 * e.g. 6 to 8 represents 6, 7, 8. A single number is a range whose start equals its end.
 */
public final class NumberRange {

    private final int start;
    private final int end;

    private NumberRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static NumberRange single(int number) {
        return new NumberRange(number, number);
    }

    public static NumberRange of(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("End (" + end + ") must not be less than start (" + start + ")");
        }
        return new NumberRange(start, end);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    /** Number of integers covered. A long, because the full int span does not fit in an int. */
    public long size() {
        return (long) end - start + 1;
    }

    /** True if {@code number} directly follows the end of this range. Overflow-safe. */
    public boolean isAdjacentTo(int number) {
        return (long) number == (long) end + 1;
    }

    /** Returns a new range that also covers {@code number}, which must directly follow this range. */
    public NumberRange extendTo(int number) {
        if (!isAdjacentTo(number)) {
            throw new IllegalArgumentException(number + " does not directly follow " + this);
        }
        return new NumberRange(start, number);
    }

    /** Expands the range into its individual numbers. */
    public List<Integer> numbers() {
        return IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberRange)) {
            return false;
        }
        NumberRange other = (NumberRange) o;
        return start == other.start && end == other.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "NumberRange[" + start + ", " + end + "]";
    }
}
