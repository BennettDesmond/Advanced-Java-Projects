package edu.pdx.cs410J.bdesmond;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client. This class helps with managing
 * the appointment book server.
 */
public class AppointmentBookRestClient extends HttpRequestHelper {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private final String url;


  /**
   * Creates a client to the appointment book REST service running on the given host and port
   *
   * @param hostName The name of the host
   * @param port     The port
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
  }

  /*
  public Map<String, String> getAllDictionaryEntries() throws IOException {
    Response response = get(this.url, Map.of());
    return Messages.parseDictionary(response.getContent());
  }
   */

  /**
   * This method gets all the appointments that are correlated to the owner's name
   * @param owner
   *        The appointment book owner's name
   * @return
   *        This is the string that contains all the appointments in the book
   * @throws IOException
   *        In the case of an error, this method can through an IOException
   */
  public String getAppointments(String owner) throws IOException {
    Response response = get(this.url, Map.of("owner", owner));
    throwExceptionIfNotOkayHttpStatus(response);
    return response.getContent();
  }

  /**
   * This method GETS the appointments that start between the two times passed to the method
   * @param owner
   *        This is the owner of the appointment book to search
   * @param start
   *        This is the lower bound
   * @param end
   *        This is the upper bound
   * @return
   *        This method returns a string that contains
   *        the appointments that fall within the search range
   * @throws IOException
   *        If an error occurs, this method can through an IOException
   */
  public String getAppointmentsBetweenTimes(String owner, String start, String end) throws IOException {
    Response response = get(this.url, Map.of("owner",owner,"start",start,"end",end));
    throwExceptionIfNotOkayHttpStatus(response);
    return response.getContent();
  }

  /**
   * This method calls POST and adds an appointment to a new or existing appointment book
   * @param owner
   *        This is the owner of the appointment book
   * @param description
   *        This is the description of the appointment
   * @param start
   *        This is the start time of the appointment
   * @param end
   *        This is the end time of the appointment
   * @throws IOException
   *        In the case of an error, this method can through an IOException
   */
  public void createAppointment(String owner, String description, String start, String end) throws IOException {
    Response response = postToMyURL(Map.of("owner", owner, "description", description, "start", start, "end", end));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * This method is a helper to call POST
   * @param appointmentInfo
   *        This is a map of the information to post
   * @return
   *        The return type is a response type
   * @throws IOException
   *        In the case of an error, this method can through an IOException
   */
  @VisibleForTesting
  Response postToMyURL(Map<String, String> appointmentInfo) throws IOException {
    return post(this.url, appointmentInfo);
  }

  /**
   * This method removes all the appointment books from the server
   * @throws IOException
   *        In the case of an error, this method can through an IOException
   */
  public void removeAllAppointmentBooks() throws IOException {
    Response response = delete(this.url, Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * This is a helper method that is called to se if an error
   * was thrown while calling the server
   * @param response
   *        This is the response value to check
   * @return
   *        If an exception is not thrown, then the response is just returned
   */
  private Response throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
    return response;
  }

}
