package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

  /**
   * Invokes the main method of {@link Project1} with the given arguments.
   */
  private MainMethodResult invokeMain(String... args) {
    return invokeMain( Project1.class, args );
  }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardError(), containsString(Project1.USAGE_MESSAGE));
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
    String message = "No description was given.\n";
    assertThat(result.getTextWrittenToStandardError(), equalTo(message));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingBeginDate() {
    MainMethodResult result = invokeMain("John","This is an event");
    assertThat(result.getTextWrittenToStandardError(), containsString("No starting date was given"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingBeginTime() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000");
    assertThat(result.getTextWrittenToStandardError(), containsString("No starting time was given"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingEndDate() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","14:39");
    assertThat(result.getTextWrittenToStandardError(), containsString("No ending date was given"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  void missingEndTime() {
    MainMethodResult result = invokeMain("John","This is an event","02/13/2000","14:39","03/13/2000");
    assertThat(result.getTextWrittenToStandardError(), containsString("No ending time was given"));
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
    //MainMethodResult result = invokeMain("-print","John","Meeting with Bernice","07/15/2021","12:00","07/15/2021","13:00");
    MainMethodResult result = invokeMain("-print","test8","This is Test 8","03/03/2021","12:00","05/04/2021","16:00");
    assertThat(result.getTextWrittenToStandardError(), emptyString());
    String message = "This is Test 8";
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

}