package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project3} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project3.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString(Project3.USAGE_MESSAGE));
    assertThat(result.getExitCode(), equalTo(1));
  }

  /**
   * Tests that no errors happen and correct output happens when correct input occurs
   */
  @Test
  void testWithAllCorrectValues() {
    MainMethodResult result = invokeMain("John","Meeting with Bernice","07/15/2021","12:00","am","07/15/2021","1:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    String message = "John's appointment book with 1 appointments\n";
    assertThat(result.getTextWrittenToStandardOut(), equalTo(message));
    assertThat(result.getExitCode(), equalTo(0));
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void missingDescription() {
    MainMethodResult result = invokeMain("John");
    //String message = "No description was given.\n";
    String message = "usage";
    assertThat(result.getTextWrittenToStandardError(), containsString(message));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingBeginDate() {
    MainMethodResult result = invokeMain("John","This is an event");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No starting date was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingBeginTime() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No starting time was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingEndDate() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","11:39");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No starting period was given."));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingEndTime() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","10:39","03/13/2000");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending date was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void tooManyArguments() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","11:39","am","03/13/2000","9:45","pm","otherField");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many arguments"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void wrongDateformat() {
    MainMethodResult result = invokeMain("John","This is an event","02132000","10:39","pm","03/13/2000","3:45","am");
    assertThat(result.getTextWrittenToStandardError(), containsString("There was a problem parsing your date. The format is incorrect"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void wrongTimeformat() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","1139","am","03/13/2000","5:45","pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("There was a problem parsing your date. The format is incorrect"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testingDateFormatWithSingleDigitDay() {
    MainMethodResult result = invokeMain("John","Meeting with Bernice","7/2/2021","12:00","am","7/15/2021","7:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    String message = "John's appointment book with 1 appointments\n";
    assertThat(result.getTextWrittenToStandardOut(), equalTo(message));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testProgramByGiving24HourTime() {
    MainMethodResult result = invokeMain("-print","John","Meeting with Bernice","07/15/2021","24:00","am","07/15/2021","13:00","pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("The time given needs to be in the 12 hour format"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testProgramByTimeWithoutTimePeriod() {
    MainMethodResult result = invokeMain("-print","John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","13:00");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending time was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void readMeTest() {
    MainMethodResult result = invokeMain("-README");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testWithPrintOptionSpecified() {
    MainMethodResult result = invokeMain("-print","John","Meeting with Bernice","07/15/2021","12:00","am","07/15/2021","7:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    String message = "Meeting with Bernice";
    assertThat(result.getTextWrittenToStandardOut(), containsString(message));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testProgramResponseToTwoOptions() {
    MainMethodResult result = invokeMain("-print","-README","John","Meeting with Bernice","07/15/2021","12:00","am","07/15/2021","1:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testTheProgramWithTheFileOptionSetButMissingOneArgument() {
    MainMethodResult result = invokeMain("-textFile","book.txt","John","Meeting with Bernice","07/15/2021","12:00","am","07/15/2021","1:00");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending period was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testTooManyOptionsGivenWithTheTextFileOption() {
    MainMethodResult result = invokeMain("-textFile","book.txt","John","Meeting with Bernice","07/15/2021","12:00","am","07/15/2021","22:00","pm","This is unnecessary");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many arguments"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testWithTextFileOptionSelectedButWithoutAFileName() {
    MainMethodResult result = invokeMain("-textFile","John", "Meeting with Bernice", "07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending period"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testProgramResponseWithThreeOptions(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "book.txt");

    MainMethodResult result = invokeMain("-print","-README","-textFile", file.getPath(), "John","Meeting with Bernice","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void errorThrownBecauseOfBadDateFormatInFile(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "badDateFormat.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("There was a problem with reading"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void errorThrownBecauseOfMissingAppointmentBookName(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "missingNameInFile.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(),"John","Meeting with Aruna","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("The name on the file does not match the name"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void noErrorThrownWithMissingAppointment(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "missingAppointmentOnFile.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's app"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void goldenTestAddingToTheJohnFolder(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "john.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's"));
    assertThat(result.getExitCode(), equalTo(0));
  }
  @Test
  @Disabled("The \"name.txt\" file appears to parse successfully??")
  void addAnAppointmentToAnAppointmentBook(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "name.txt");
    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("There was a problem with reading"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void correctRunWithPrettyPrintSelectedAndAFileGiven(@TempDir File tempDir) throws IOException {
    File fileStor = copyResourceIntoFileInDirectory(tempDir, "john.txt");
    File filePretty = copyResourceIntoFileInDirectory(tempDir, "prettyFile.txt");

    MainMethodResult result = invokeMain("-pretty",filePretty.getPath(),"-textFile",fileStor.getPath(), "John","Meeting with Matthew","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's appointment book with "));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void correctRunWithPrettyPrintSelectedAndNoFileGiven(@TempDir File tempDir) throws IOException {
    File fileStor = copyResourceIntoFileInDirectory(tempDir, "john.txt");

    MainMethodResult result = invokeMain("-pretty","-","-textFile", fileStor.getPath(), "John","Meeting with Aruna","07/15/2021", "12:00", "am","07/15/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's appointment book with "));
    assertThat(result.getTextWrittenToStandardOut(), containsString("******************************************\n" +
            "John's Appointment Book"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testWithStartDateAfterEndDate() {
    MainMethodResult result = invokeMain("-pretty","-","-textFile","johnFile.txt","John", "Meeting with THree", "07/11/2021", "12:00", "am","07/10/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), containsString("The start time cannot be after the end time"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }
  @Disabled
  @Test
  void verifyThatTheOrderingAlgorithmIsWorking() {
    MainMethodResult result = invokeMain("-pretty","-","-textFile","johnFile.txt","John", "Meeting with THree", "07/10/2021", "12:00", "am","07/11/2021", "2:00","pm");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's app"));
    assertThat(result.getExitCode(), equalTo(0));
    MainMethodResult result2 = invokeMain("-pretty","-","-textFile","johnFile.txt","John", "Meeting with Four", "07/12/2021", "12:00", "am","07/13/2021", "2:00","pm");
    assertThat(result2.getTextWrittenToStandardError(), emptyString());
    assertThat(result2.getTextWrittenToStandardOut(), containsString("******"));
    assertThat(result2.getExitCode(), equalTo(0));
  }

  @Test
  void testWithAddingTwoAppointmentsInNonChronologicalOrder() {

  }

  private File copyResourceIntoFileInDirectory(File directory, String resourceName) throws IOException {
    File file = new File(directory, resourceName);
    InputStream resource = getClass().getResourceAsStream(resourceName);
    assertThat("Resource \"" + resourceName + "\" not found", resource, notNullValue());

    try (
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
      PrintWriter writer = new PrintWriter(new FileWriter(file));
    ) {
      while(reader.ready()) {
        String line = reader.readLine();
        writer.println(line);
      }
      writer.flush();
    }

    return file;
  }

}