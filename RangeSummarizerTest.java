package numberrangesummarizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;

/**
 * End to end tests of the provided interface. The "assumption" tests document every
 * interpretation of the requirement made in {@link RangeSummarizer}.
 */
public class RangeSummarizerTest {

    private final NumberRangeSummarizer summarizer = new RangeSummarizer();

    private String summarize(String input) {
        return summarizer.summarizeCollection(summarizer.collect(input));
    }

    // ---- The requirement as given ----

    @Test
    public void providedExample() {
        assertEquals("1, 3, 6-8, 12-15, 21-24, 31", summarize("1,3,6,7,8,12,13,14,15,21,22,23,24,31"));
    }

    @Test
    public void collectReturnsTheParsedNumbers() {
        Collection<Integer> numbers = summarizer.collect("1,3,6,7,8");
        assertEquals(Arrays.asList(1, 3, 6, 7, 8), numbers);
    }

    // ---- Assumptions ----

    @Test
    public void assumption_outputIsSortedAscending() {
        assertEquals("1-3, 9", summarize("9,3,1,2"));
    }

    @Test
    public void assumption_duplicatesAreCollapsed() {
        assertEquals("1-3, 5", summarize("1,1,2,2,3,5,5"));
    }

    @Test
    public void assumption_aRangeNeedsAtLeastThreeNumbers() {
        assertEquals("1, 2", summarize("1,2"));
        assertEquals("1-3", summarize("1,2,3"));
    }

    @Test
    public void assumption_minimumRangeLengthCanBeChangedByInjectingAFormatter() {
        NumberRangeSummarizer pairsCollapsed =
                new RangeSummarizer(new NumberListParser(), new RangeGrouper(), new RangeFormatter(2));
        assertEquals("1-2, 5", pairsCollapsed.summarizeCollection(Arrays.asList(1, 2, 5)));
    }

    @Test
    public void assumption_sequentialMeansExactlyOneMoreThanThePrevious() {
        assertEquals("1, 3, 5", summarize("1,3,5"));
    }

    @Test
    public void assumption_negativeNumbersAndZeroAreValid() {
        assertEquals("-3-0, 5", summarize("-3,-2,-1,0,5"));
    }

    @Test
    public void assumption_whitespaceAndEmptyEntriesAreIgnored() {
        assertEquals("1-3, 5", summarize(" 1, 2 ,3,, 5,"));
    }

    @Test
    public void assumption_nullOrBlankInputGivesEmptyResultNotAnError() {
        assertTrue(summarizer.collect(null).isEmpty());
        assertTrue(summarizer.collect("  ").isEmpty());
        assertEquals("", summarizer.summarizeCollection(null));
        assertEquals("", summarizer.summarizeCollection(Collections.<Integer>emptyList()));
    }

    @Test
    public void assumption_nullElementsInACollectionAreIgnored() {
        assertEquals("1, 2", summarizer.summarizeCollection(Arrays.asList(1, null, 2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void assumption_nonIntegerInputIsAnError() {
        summarizer.collect("1,two,3");
    }

    @Test
    public void assumption_collectOnlyParsesAndKeepsOrderAndDuplicates() {
        assertEquals(Arrays.asList(3, 1, 1), summarizer.collect("3,1,1"));
    }

    // ---- Robustness ----

    @Test
    public void singleNumber() {
        assertEquals("5", summarize("5"));
    }

    @Test
    public void wholeInputIsOneRange() {
        assertEquals("1-10", summarize("1,2,3,4,5,6,7,8,9,10"));
    }

    @Test
    public void handlesIntegerMaxWithoutOverflow() {
        Collection<Integer> input = Arrays.asList(Integer.MAX_VALUE - 2, Integer.MAX_VALUE - 1, Integer.MAX_VALUE);
        assertEquals((Integer.MAX_VALUE - 2) + "-" + Integer.MAX_VALUE, summarizer.summarizeCollection(input));
    }

    @Test(expected = NullPointerException.class)
    public void constructorRejectsNullCollaborators() {
        new RangeSummarizer(null, new RangeGrouper(), new RangeFormatter());
    }
}
