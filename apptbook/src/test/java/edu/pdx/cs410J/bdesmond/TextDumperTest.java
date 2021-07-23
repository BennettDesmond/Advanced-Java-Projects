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

public class TextDumperTest {

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
    void verifyThatTheCorrectBehaviorHappensWhenTheDefaultConstructorIsCalled() {
        TextDumper dumper = new TextDumper();
        assertThat(dumper.fileVerificationForDumper(), equalTo(false));
    }

    @Test
    void verifyCorrectConstructorWithParametersIsCalled(@TempDir File tempDir) throws IOException {
        String fileName = "name.txt";
        File file = new File(tempDir, fileName);
        assertThat(file.createNewFile(), equalTo(true));

        TextDumper dumper = new TextDumper(file.getPath());
        AppointmentBook book = new AppointmentBook();
        assertThat(dumper.fileVerificationForDumper(), equalTo(true));
    }

    @Test
    void verifyThatTheWriterDoesntWriteToABadFileName() {
        TextDumper dumper = new TextDumper();
        AppointmentBook book = new AppointmentBook();
        assertThat(dumper.writeToFile(book), equalTo(false));
    }

    @Test
    void verifyTheReturnValueFromACorrectFileWriterRun(@TempDir File tempDir) {
        String fileName = "name.txt";
        File file = new File(tempDir, fileName);

        TextDumper dumper = new TextDumper(file.getPath());
        AppointmentBook book = createAppointmentBook();
        assertThat(dumper.writeToFile(book), equalTo(true));
    }

    @Test
    void verifyThatIfDumpIsPassedABadFileNameExceptionIsThrown() {
        TextDumper dumper = new TextDumper();
        AppointmentBook book = new AppointmentBook();
        assertThrows(IOException.class, () -> {dumper.dump(book);});
    }

    @Test
    void verifyThatNoExceptionsAreThrownWhenCorrectInformationIsPassed(@TempDir File tempDir) throws IOException {
        String fileName = "name.txt";
        File file = new File(tempDir, fileName);
        assertThat(file.createNewFile(), equalTo(true));

        TextDumper dumper = new TextDumper(file.getPath());
        AppointmentBook book = createAppointmentBook();
        assertDoesNotThrow(() -> {dumper.dump(book);});
    }

    private AppointmentBook createAppointmentBook() {
        Appointment app = new Appointment(setupDateObject("07/15/2021 12:00 am"),setupDateObject("07/15/2021 12:00 pm"),"Meeting with Barb");
        LinkedList appLL = new LinkedList();
        appLL.add(app);
        AppointmentBook book = new AppointmentBook("John",appLL);
        return book;
    }

}
