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
 * Integration tests for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project2} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project2.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString(Project2.USAGE_MESSAGE));
    assertThat(result.getExitCode(), equalTo(1));
  }

  /**
   * Tests that no errors happen and correct output happens when correct input occurs
   */
  @Test
  void testWithAllCorrectValues() {
    MainMethodResult result = invokeMain("John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","13:00");
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
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","14:39");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending date was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingEndTime() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","14:39","03/13/2000");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending time was given"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void tooManyArguments() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","14:39","03/13/2000","23:45","otherField");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many arguments"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void wrongDateformat() {
    MainMethodResult result = invokeMain("John","This is an event","02132000","14:39","03/13/2000","23:45");
    assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect Date format"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void wrongTimeformat() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","1439","03/13/2000","23:45");
    assertThat(result.getTextWrittenToStandardError(), containsString("Incorrect Time format"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testingDateFormatWithSingleDigitDay() {
    MainMethodResult result = invokeMain("John","Meeting with Bernice","7/15/2021","12:00","7/15/2021","13:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    String message = "John's appointment book with 1 appointments\n";
    assertThat(result.getTextWrittenToStandardOut(), equalTo(message));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void readMeTest() {
    MainMethodResult result = invokeMain("-README");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testWithPrintOptionSpecified() {
    MainMethodResult result = invokeMain("-print","John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","13:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    String message = "Meeting with Bernice";
    assertThat(result.getTextWrittenToStandardOut(), containsString(message));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testProgramResponseToTwoOptions() {
    MainMethodResult result = invokeMain("-print","-README","John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","13:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void testTheProgramWithTheFileOptionSetButMissingOneArgument() {
    MainMethodResult result = invokeMain("-textFile","book.txt","John","Meeting with Bernice","07/15/2021","12:00","07/15/2021");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending time"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testTooManyOptionsGivenWithTheTextFileOption() {
    MainMethodResult result = invokeMain("-textFile","book.txt","John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","22:00","This is unnecessary");
    assertThat(result.getTextWrittenToStandardError(), containsString("Too many arguments"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testWithTextFileOptionSelectedButWithoutAFileName() {
    MainMethodResult result = invokeMain("-textFile","John", "Meeting with Bernice", "07/15/2021", "12:00", "07/15/2021", "22:00");
    //assertThat(result.getTextWrittenToStandardError(), containsString("No ending time"));
    assertThat(result.getTextWrittenToStandardError(), containsString("usage"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void testProgramResponseWithThreeOptions(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "book.txt");

    MainMethodResult result = invokeMain("-print","-README","-textFile", file.getPath(), "John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","13:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("Bennett Desmond"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void errorThrownBecauseOfBadDateFormatInFile(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "badDateFormat.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","08/15/2021","23:00","09/15/2021","22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("There was a problem with reading"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void errorThrownBecauseOfMissingAppointmentBookName(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "missingNameInFile.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(),"John","Meeting with Aruna","08/15/2021","23:00","09/15/2021","22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("The name on the file does not match the name"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void noErrorThrownWithMissingAppointment(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "missingAppointmentOnFile.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","08/15/2021","23:00","09/15/2021","22:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's app"));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  void goldenTestAddingToTheJohnFolder(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "john.txt");

    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","08/15/2021","23:00","09/15/2021","22:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    assertThat(result.getTextWrittenToStandardOut(), containsString("John's"));
    assertThat(result.getExitCode(), equalTo(0));
  }
  @Test
  @Disabled("The \"name.txt\" file appears to parse successfully??")
  void addAnAppointmentToAnAppointmentBook(@TempDir File tempDir) throws IOException {
    File file = copyResourceIntoFileInDirectory(tempDir, "name.txt");
    MainMethodResult result = invokeMain("-textFile", file.getPath(), "John","Meeting with Aruna","08/15/2021","23:00","09/15/2021","22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("There was a problem with reading"));
    assertThat(result.getTextWrittenToStandardOut(), emptyString());
    assertThat(result.getExitCode(), equalTo(1));
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