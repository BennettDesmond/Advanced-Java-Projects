package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@TestMethodOrder(MethodName.class)
class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  @Test
  void test0RemoveAllAppointmentBooks() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    client.removeAllAppointmentBooks();
  }

  /*
  @Test
  void test1EmptyServerContainsNoDictionaryEntries() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Map<String, String> dictionary = client.getAllDictionaryEntries();
    assertThat(dictionary.size(), equalTo(0));
  }
  */

  @Test
  void test2CreateAppointmentBookWithOneAppointment() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "John";
    String description = "Meet with Matt";
    String start = "3/20/21 6:28 PM";
    String end = "3/21/21 6:30 PM";
    client.createAppointment(owner, description, start, end);

    String appointmentBookText = client.getAppointments(owner);
    assertThat(appointmentBookText, containsString(owner));
    assertThat(appointmentBookText, containsString(description));
    assertThat(appointmentBookText, containsString(start));
    assertThat(appointmentBookText, containsString(end));
  }

  @Test
  void test4MissingRequiredParameterReturnsPreconditionFailed() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    HttpRequestHelper.Response response = client.postToMyURL(Map.of());
    assertThat(response.getContent(), containsString("Precondition Failed"));
    assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
  }

  @Test
  void test5GetAppointmentsBetweenBeginAndEndTime() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    client.createAppointment("John", "Meet with Viju", "1/20/21 6:28 PM", "1/21/21 6:30 PM");
    client.createAppointment("John", "Meet with Aruna", "5/20/21 6:28 PM", "5/21/21 6:30 PM");

    String owner = "John";
    String start = "3/11/21 6:28 PM";
    String end = "4/13/21 6:30 PM";
    String appointmentBookText = client.getAppointmentsBetweenTimes(owner, start, end);

    assertThat(appointmentBookText, containsString("John"));
    assertThat(appointmentBookText, containsString("Meet with Matt"));
    assertThat(appointmentBookText, containsString("3/20/21 6:28 PM"));
    assertThat(appointmentBookText, containsString("3/21/21 6:30 PM"));
  }

}
