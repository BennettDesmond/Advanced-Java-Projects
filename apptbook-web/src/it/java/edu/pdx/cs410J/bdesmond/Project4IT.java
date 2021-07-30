package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testng.reporters.jq.Main;

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
        assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    }

    @Disabled
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

    @Test
    void test4BasicTestWithAddingAnAppointment() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John","This is an event in December","12/23/21","12:30","am","12/24/21","8:30","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), containsString(""));
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John");
        //assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), containsString(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in December"));
    }

    @Test
    void test5AddTwoMoreAppointmentsAndMakeSureNothingDisappears() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John","This is an event in January","1/23/21","8:30","am","1/24/21","8:30","pm");
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John","This is an event in March","3/23/21","8:30","am","3/24/21","8:30","pm");
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in December"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in January"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in March"));
    }

    @Test
    void test6VerifyTheSearchFunction() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","John","3/1/21","1:00","am","3/25/21","11:59","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in March"));
    }

    @Test
    void test7TestTheSearchMethodWithNoEventsInRange() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","John","3/1/20","1:00","am","3/25/20","11:59","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("There are no appointments that are within this range"));
    }

    @Test
    void test8VerifyThatThePrintFunctionBehavesAsExpected() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-print","John","Descriptions are the best and they are rarely read","2/1/21","1:00","am","2/25/21","11:59","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Descriptions are the best and they are rarely read"));
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Descriptions are the best and they are rarely read"));
    }

    @Test
    void test9TestTheREADME() {
        MainMethodResult result = invokeMain(Project4.class,"-README","-host",HOSTNAME,"-port",PORT,"-search","John","3/1/21","1:00","am","3/25/21","11:59","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    }

    @Test
    void test10AddASecondAppointmentBookAndVerifyThatBothExistSimultaneously() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"Billy","This is an event in August for Billy","8/1/21","8:00","am","8/25/21","11:59","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"Billy");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in August for Billy"));
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John","This is an event in August for John","8/1/21","8:00","am","8/25/21","11:59","pm");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"John");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an event in August for John"));
    }

    //TODO BELOW
    //TOO Many arguments
    @Test
    void test11PassTooManyArguments() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","John","3/1/21","1:00","am","3/25/21","11:59","pm","this is a value");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Too"));
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
    }

    //Wrong time format
    @Test
    void test12PassingTheWrongDateFormat() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","John","78/1/21","25:00","am","3/25/21","3:59","pm");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("The date is in the wrong format"));
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
    }

    //Try accessing all appointments for person that does not exist
    @Test
    void test13AccessAllAppointmentsForNonExistentPerson() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"Wolfgang");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("The person you are trying to"));
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
    }

    @Test
    void test14AccessAllAppointmentsForNonExistentPerson() {
        MainMethodResult result = invokeMain(Project4.class,"-host",HOSTNAME,"-port",PORT,"-search","Amadeus","3/1/21","11:00","am","3/25/21","3:59","pm");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("The person you are trying to search for does not exist in the server"));
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
    }
}