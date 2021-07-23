package edu.pdx.cs410J.bdesmond;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrettyPrinterTest {

    static private Date setupDateObject(String date) {
        Date dateClassObj = new Date();
        DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
        try {
            dateClassObj = format.parse(date);
        } catch(ParseException e) {
            System.err.println(e);
        }
        return dateClassObj;
    }

    @Test
    void returnsIOExceptionWhenBadFileIsPassed() {
        PrettyPrinter printer = new PrettyPrinter("",true);
        AppointmentBook appBook = createAppointmentBook();
        assertThrows(IOException.class, () -> {printer.dump(appBook);});
    }

    @Test
    void verifyCorrectConstructorWithParametersIsCalled(@TempDir File tempDir) throws IOException {
        String fileName = "name.txt";
        File file = new File(tempDir, fileName);
        assertThat(file.createNewFile(), equalTo(true));

        PrettyPrinter printer = new PrettyPrinter(file.getPath(),true);
        assertThat(printer.fileVerification(), equalTo(true));
    }

    @Test
    void verifyThatTheWriterDoesntWriteToABadFileName() {
        PrettyPrinter printer = new PrettyPrinter();
        AppointmentBook book = new AppointmentBook();
        assertThat(printer.prettyPrintToFile(book), equalTo(false));
    }

    @Test
    void verifyTheReturnValueFromACorrectFileWriterRun(@TempDir File tempDir) {
        String fileName = "name.txt";
        File file = new File(tempDir, fileName);

        PrettyPrinter printer = new PrettyPrinter(file.getPath(),true);
        AppointmentBook book = createAppointmentBook();
        assertThat(printer.prettyPrintToFile(book), equalTo(true));
    }

    @Test
    void verifyThatIfPrinterIsPassedABadFileNameExceptionIsThrown() {
        PrettyPrinter printer = new PrettyPrinter();
        AppointmentBook book = new AppointmentBook();
        assertDoesNotThrow(() -> {printer.dump(book);});
    }

    @Test
    void verifyThatIfDumpIsPassedABadFileNameExceptionIsThrown() {
        PrettyPrinter printer = new PrettyPrinter();
        AppointmentBook book = new AppointmentBook();
        assertDoesNotThrow(() -> {printer.dump(book);});
    }

    @Test
    void verifyThatNoExceptionsAreThrownWhenCorrectInformationIsPassed(@TempDir File tempDir) throws IOException {
        String fileName = "name.txt";
        File file = new File(tempDir, fileName);
        assertThat(file.createNewFile(), equalTo(true));

        PrettyPrinter printer = new PrettyPrinter(file.getPath(),true);
        AppointmentBook book = createAppointmentBook();
        assertDoesNotThrow(() -> {printer.dump(book);});
    }



    private AppointmentBook createAppointmentBook() {
        Appointment app = new Appointment(setupDateObject("07/15/2021 12:00 am"),setupDateObject("07/15/2021 12:00 pm"),"Meeting with Barb");
        LinkedList appLL = new LinkedList();
        appLL.add(app);
        AppointmentBook book = new AppointmentBook("John",appLL);
        return book;
    }

}