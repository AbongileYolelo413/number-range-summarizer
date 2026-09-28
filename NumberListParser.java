package numberrangesummarizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** Parses a comma separated string of integers into a list, preserving input order. */
public class NumberListParser {

    private static final String DELIMITER = ",";

    /**
     * @param input e.g. "1, 3,6,7"; null or blank gives an empty list. Whitespace around
     *              values is ignored and empty entries (e.g. a trailing comma) are skipped.
     * @throws IllegalArgumentException if an entry is not a valid int
     */
    public List<Integer> parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> numbers = Arrays.stream(input.split(DELIMITER))
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .map(NumberListParser::toInteger)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(numbers);
    }

    private static Integer toInteger(String token) {
        try {
            return Integer.valueOf(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a valid integer: '" + token + "'", e);
        }
    }
}
