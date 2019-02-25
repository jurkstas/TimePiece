package timepiece;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeNamesEnglishTest {

    @Test
    void getTimeStrings(TestReporter reporter) {
        List<String>[][] timeStrings = TimeNamesEnglish.getTimeStrings();

        assertEquals(12, timeStrings.length);
        assertTrue(Arrays.stream(timeStrings).allMatch((arr) -> arr.length == 12));

        reporter.publishEntry("first dimension", String.valueOf(timeStrings.length));
        reporter.publishEntry("second dimension", Arrays.stream(timeStrings).map((arr) -> String.valueOf(arr.length)).reduce("", (a, b) -> a + ", " + b));

        for (int i = 0; i < timeStrings.length; i++) {
            for (int j = 0; j < timeStrings[i].length; j++) {
                reporter.publishEntry("(" + i + "," + j + ")", String.valueOf(timeStrings[i][j]));
            }
        }
    }
}
