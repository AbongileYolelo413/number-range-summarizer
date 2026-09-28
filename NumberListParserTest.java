package numberrangesummarizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;

public class NumberListParserTest {

    private final NumberListParser parser = new NumberListParser();

    @Test
    public void parsesCommaSeparatedNumbersInInputOrder() {
        assertEquals(Arrays.asList(3, 1, 2), parser.parse("3,1,2"));
    }

    @Test
    public void keepsDuplicates() {
        assertEquals(Arrays.asList(1, 1, 2), parser.parse("1,1,2"));
    }

    @Test
    public void ignoresWhitespaceAroundNumbers() {
        assertEquals(Arrays.asList(1, 2, 5), parser.parse(" 1, 2 ,\t5 "));
    }

    @Test
    public void skipsEmptyEntries() {
        assertEquals(Arrays.asList(1, 2), parser.parse("1,,2,"));
    }

    @Test
    public void parsesNegativeNumbersAndZero() {
        assertEquals(Arrays.asList(-2, 0, 4), parser.parse("-2,0,4"));
    }

    @Test
    public void nullBlankAndDelimiterOnlyInputGiveEmptyList() {
        assertTrue(parser.parse(null).isEmpty());
        assertTrue(parser.parse("").isEmpty());
        assertTrue(parser.parse("   ").isEmpty());
        assertTrue(parser.parse(",,").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonNumericEntry() {
        parser.parse("1,two,3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsDecimalEntry() {
        parser.parse("1,2.5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNumberTooLargeForInt() {
        parser.parse("99999999999");
    }
}
