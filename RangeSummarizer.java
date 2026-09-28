package numberrangesummarizer;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Produces a comma delimited list of numbers, grouping sequential numbers into ranges.
 * Example: "1,3,6,7,8,12,13,14,15,21,22,23,24,31" becomes "1, 3, 6-8, 12-15, 21-24, 31".
 *
 * <p>The work is split between collaborators: {@link NumberListParser} (text to numbers),
 * {@link RangeGrouper} (numbers to ranges) and {@link RangeFormatter} (ranges to text).
 *
 * <p>Assumptions (each one is pinned down by a test in RangeSummarizerTest):
 * <ol>
 *   <li>The output is sorted ascending, whatever order the input was in.</li>
 *   <li>Duplicates are collapsed: 1,1,2 is treated as 1,2.</li>
 *   <li>"Sequential" means consecutive integers (each is exactly 1 more than the last).</li>
 *   <li>A range needs at least 3 numbers. A pair such as 1,2 is listed as "1, 2", not "1-2".
 *       This is configurable via {@link RangeFormatter}.</li>
 *   <li>Negative numbers and zero are valid input.</li>
 *   <li>Whitespace around numbers is ignored, and empty entries (e.g. "1,,2" or a trailing comma) are skipped.</li>
 *   <li>Null or blank input gives an empty collection / empty string rather than an error.</li>
 *   <li>Null elements inside a collection passed to summarizeCollection are ignored.</li>
 *   <li>An entry that is not a valid int is an error: IllegalArgumentException.</li>
 *   <li>collect() only parses; it keeps input order and duplicates. Normalising happens in summarizeCollection().</li>
 * </ol>
 */
public class RangeSummarizer implements NumberRangeSummarizer {

    private final NumberListParser parser;
    private final RangeGrouper grouper;
    private final RangeFormatter formatter;

    public RangeSummarizer() {
        this(new NumberListParser(), new RangeGrouper(), new RangeFormatter());
    }

    public RangeSummarizer(NumberListParser parser, RangeGrouper grouper, RangeFormatter formatter) {
        this.parser = Objects.requireNonNull(parser, "parser");
        this.grouper = Objects.requireNonNull(grouper, "grouper");
        this.formatter = Objects.requireNonNull(formatter, "formatter");
    }

    @Override
    public Collection<Integer> collect(String input) {
        return parser.parse(input);
    }

    @Override
    public String summarizeCollection(Collection<Integer> input) {
        List<NumberRange> ranges = grouper.group(input);
        return formatter.format(ranges);
    }
}
