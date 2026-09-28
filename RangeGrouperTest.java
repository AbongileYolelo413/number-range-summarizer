package numberrangesummarizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class RangeGrouperTest {

    private final RangeGrouper grouper = new RangeGrouper();

    @Test
    public void emptyAndNullGiveNoRanges() {
        assertTrue(grouper.group(Collections.<Integer>emptyList()).isEmpty());
        assertTrue(grouper.group(null).isEmpty());
    }

    @Test
    public void nonConsecutiveNumbersStaySeparate() {
        assertEquals(
                Arrays.asList(NumberRange.single(1), NumberRange.single(3), NumberRange.single(5)),
                grouper.group(Arrays.asList(1, 3, 5)));
    }

    @Test
    public void consecutiveNumbersMergeIntoOneRange() {
        assertEquals(Collections.singletonList(NumberRange.of(1, 4)), grouper.group(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void mixedInputGivesRangesAndSingles() {
        assertEquals(
                Arrays.asList(NumberRange.single(1), NumberRange.of(3, 5), NumberRange.single(9)),
                grouper.group(Arrays.asList(1, 3, 4, 5, 9)));
    }

    @Test
    public void sortsBeforeGrouping() {
        assertEquals(Arrays.asList(NumberRange.of(1, 3), NumberRange.single(9)),
                grouper.group(Arrays.asList(9, 3, 1, 2)));
    }

    @Test
    public void removesDuplicates() {
        assertEquals(Collections.singletonList(NumberRange.of(1, 3)), grouper.group(Arrays.asList(1, 1, 2, 2, 3)));
    }

    @Test
    public void ignoresNullElements() {
        assertEquals(Collections.singletonList(NumberRange.of(1, 2)), grouper.group(Arrays.asList(1, null, 2)));
    }

    @Test
    public void groupsAcrossZeroAndNegatives() {
        assertEquals(Collections.singletonList(NumberRange.of(-1, 1)), grouper.group(Arrays.asList(-1, 0, 1)));
    }

    @Test
    public void handlesIntegerBoundariesWithoutOverflow() {
        assertEquals(
                Arrays.asList(NumberRange.single(Integer.MIN_VALUE), NumberRange.single(Integer.MAX_VALUE)),
                grouper.group(Arrays.asList(Integer.MAX_VALUE, Integer.MIN_VALUE)));
    }
}
