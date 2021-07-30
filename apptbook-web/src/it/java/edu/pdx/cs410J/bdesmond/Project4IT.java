package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    void test0RemoveAllMappings() throws IOException {
      AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
      client.removeAllAppointmentBooks();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project4.class);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.MISSING_ARGS));
    }

    @Test
    void test2EmptyServer() {
        MainMethodResult result = invokeMain(Project4.class, "-host",HOSTNAME,"-port",PORT );
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(1));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(""));
    }

    @Test
    void test3MissingDescriptionShouldFail() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John","12/23/21","8:30","am","12/24/21","8:30","pm");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo("dsfag"));
    }

    @Test
    void test4AddDefinition() {
        String word = "WORD";
        String definition = "DEFINITION";

        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT, word, definition );
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(Messages.definedWordAs(word, definition)));

        result = invokeMain( Project4.class, HOSTNAME, PORT, word );
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(Messages.formatDictionaryEntry(word, definition)));

        result = invokeMain( Project4.class, HOSTNAME, PORT );
        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(Messages.formatDictionaryEntry(word, definition)));
    }
}