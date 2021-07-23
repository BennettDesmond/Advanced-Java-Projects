package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {

    @Test
    void verifyThatIfParseIsPassedABadFileNameExceptionIsThrown() {
        TextParser parser = new TextParser();
        assertDoesNotThrow(parser::parse);
    }

    @Test
    void verifyThatNoExceptionIsThrownWhenEmptyFileIsPassed(@TempDir File tempDir) {
        String fileName = "emptyFile.txt";
        File file = new File(tempDir, fileName);

        TextParser parser = new TextParser(file.getPath());
        assertDoesNotThrow(parser::parse);
    }

    @Test
    void verifyThatNullIsPassedWhenAnIncorrectTimeIsPassedThatIsIn24HourTime() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("07/11/2021 14:39"), equalTo(null));
    }

    @Test
    void verifyThatNullIsPassedWhenAnIncorrectTimeIsPassedWithoutTheTimePeriod() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("7/11/2021 4:39"), equalTo(null));
    }

    @Test
    void verifyThatTrueIsPassedWhenACorrectTimeIsPassedWithNoLeadingNumbersOnFirst() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("7/11/2021 12:39 pm"), notNullValue());
    }

    @Test
    void verifyThatNullIsPassedWhenAnIncorrectTimeIsPassedWithoutTheTimePeriodPartButWithTwoDigitMonth() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("12/11/2021 4:39"), equalTo(null));
    }

    @Test
    void verifyThatFalseIsPassedWhenAnIncorrectTimeIsPassed() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("7/11/2021 25:39 am"), equalTo(null));
    }

    @Test
    void verifyThatNullIsPassedWhenAnIncorrectDateIsPassedDay() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("12/32/2021 12:39 aM"), equalTo(null));
    }

    @Test
    void verifyThatFalseIsPassedWhenAnIncorrectDateIsPassedMonth() {
        TextParser parser = new TextParser();
        assertThat(parser.validateTime("13/31/2021 12:39 am"), equalTo(null));
    }

    //Write IT Tests once the dumper is implemented

}
