package numberrangesummarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Groups numbers into the smallest list of {@link NumberRange}s that covers them exactly. */
public class RangeGrouper {

    /**
     * Sorts and de-duplicates the numbers (null elements are ignored), then merges
     * consecutive values. Every run is returned, including runs of length one.
     */
    public List<NumberRange> group(Collection<Integer> numbers) {
        if (numbers == null) {
            return Collections.emptyList();
        }
        List<Integer> sorted = numbers.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<NumberRange> ranges = new ArrayList<>();
        NumberRange current = null;
        for (int number : sorted) {
            if (current != null && current.isAdjacentTo(number)) {
                current = current.extendTo(number);
            } else {
                if (current != null) {
                    ranges.add(current);
                }
                current = NumberRange.single(number);
            }
        }
        if (current != null) {
            ranges.add(current);
        }
        return ranges;
    }
}
