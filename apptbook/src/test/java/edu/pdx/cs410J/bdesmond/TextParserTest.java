package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {

    @Test
    void verifyThatIfParseIsPassedABadFileNameExceptionIsThrown() {
        TextParser parser = new TextParser();
        assertThrows(ParserException.class, parser::parse);
    }

    @Test
    void verifyThatNoExceptionsAreThrownWhenCorrectInformationIsPassedToParse() {
        String fileName = "name";
        TextParser parser = new TextParser(fileName);
        assertDoesNotThrow(parser::parse);

    }

}
