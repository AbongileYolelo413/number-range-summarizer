package numberrangesummarizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;

public class NumberRangeTest {

    @Test
    public void singleHasSameStartAndEndAndSizeOne() {
        NumberRange range = NumberRange.single(5);
        assertEquals(5, range.getStart());
        assertEquals(5, range.getEnd());
        assertEquals(1L, range.size());
    }

    @Test
    public void ofCoversInclusiveSpan() {
        NumberRange range = NumberRange.of(6, 8);
        assertEquals(3L, range.size());
        assertEquals(Arrays.asList(6, 7, 8), range.numbers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ofRejectsEndBeforeStart() {
        NumberRange.of(8, 6);
    }

    @Test
    public void adjacencyOnlyTrueForNextInteger() {
        NumberRange range = NumberRange.of(1, 3);
        assertTrue(range.isAdjacentTo(4));
        assertFalse(range.isAdjacentTo(3));
        assertFalse(range.isAdjacentTo(5));
    }

    @Test
    public void adjacencyDoesNotOverflowAtIntegerMax() {
        assertFalse(NumberRange.single(Integer.MAX_VALUE).isAdjacentTo(Integer.MIN_VALUE));
    }

    @Test
    public void extendToReturnsNewRangeAndLeavesOriginalUnchanged() {
        NumberRange original = NumberRange.of(1, 2);
        NumberRange extended = original.extendTo(3);
        assertEquals(NumberRange.of(1, 3), extended);
        assertEquals(NumberRange.of(1, 2), original);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendToRejectsNonAdjacentNumber() {
        NumberRange.of(1, 2).extendTo(5);
    }

    @Test
    public void sizeOfFullIntSpanDoesNotOverflow() {
        assertEquals(4294967296L, NumberRange.of(Integer.MIN_VALUE, Integer.MAX_VALUE).size());
    }

    @Test
    public void equalityIsBasedOnStartAndEnd() {
        assertEquals(NumberRange.of(1, 3), NumberRange.of(1, 3));
        assertEquals(NumberRange.of(1, 3).hashCode(), NumberRange.of(1, 3).hashCode());
        assertFalse(NumberRange.of(1, 3).equals(NumberRange.of(1, 4)));
        assertEquals(NumberRange.single(2), NumberRange.of(2, 2));
    }
}
