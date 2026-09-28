package numberrangesummarizer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class RangeFormatterTest {

    private final RangeFormatter formatter = new RangeFormatter();

    @Test
    public void emptyAndNullGiveEmptyString() {
        assertEquals("", formatter.format(Collections.<NumberRange>emptyList()));
        assertEquals("", formatter.format(null));
    }

    @Test
    public void singleNumberIsWrittenPlainly() {
        assertEquals("5", formatter.format(Collections.singletonList(NumberRange.single(5))));
    }

    @Test
    public void rangeOfThreeIsCollapsed() {
        assertEquals("6-8", formatter.format(Collections.singletonList(NumberRange.of(6, 8))));
    }

    @Test
    public void rangeOfTwoIsListedIndividuallyByDefault() {
        assertEquals("1, 2", formatter.format(Collections.singletonList(NumberRange.of(1, 2))));
    }

    @Test
    public void itemsAreSeparatedByCommaAndSpace() {
        assertEquals("1-3, 5, 7-9",
                formatter.format(Arrays.asList(NumberRange.of(1, 3), NumberRange.single(5), NumberRange.of(7, 9))));
    }

    @Test
    public void minimumRangeLengthIsConfigurable() {
        assertEquals("1-2", new RangeFormatter(2).format(Collections.singletonList(NumberRange.of(1, 2))));
        assertEquals("1, 2, 3", new RangeFormatter(4).format(Collections.singletonList(NumberRange.of(1, 3))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMinimumRangeLengthBelowTwo() {
        new RangeFormatter(1);
    }

    @Test
    public void negativeNumbersAreWrittenWithTheirSign() {
        assertEquals("-3-0", formatter.format(Collections.singletonList(NumberRange.of(-3, 0))));
    }
}
