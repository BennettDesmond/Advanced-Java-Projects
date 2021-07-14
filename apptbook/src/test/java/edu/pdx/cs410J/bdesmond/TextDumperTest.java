package edu.pdx.cs410J.bdesmond;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextDumperTest {

    @Test
    void verifyThatTheCorrectBehaviorHappensWhenTheDefaultConstructorIsCalled() {
        TextDumper dumper = new TextDumper();
        assertThat(dumper.fileVerification(), equalTo(false));
    }

    @Test
    void verifyCorrectConstructorWithParametersIsCalled() {
        String fileName = "name";
        TextDumper dumper = new TextDumper(fileName);
        AppointmentBook book = new AppointmentBook();
        assertThat(dumper.fileVerification(), equalTo(true));
    }

    @Test
    void verifyThatTheWriterDoesntWriteToABadFileName() {
        TextDumper dumper = new TextDumper();
        AppointmentBook book = new AppointmentBook();
        assertThat(dumper.writeToFile(book), equalTo(false));
    }

    @Test
    void verifyTheReturnValueFromACorrectFileWriterRun() {
        String fileName = "name";
        TextDumper dumper = new TextDumper(fileName);
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
    void verifyThatNoExceptionsAreThrownWhenCorrectInformationIsPassed() {
        String fileName = "name";
        TextDumper dumper = new TextDumper(fileName);
        AppointmentBook book = createAppointmentBook();
        assertDoesNotThrow(() -> {dumper.dump(book);});
    }

    private AppointmentBook createAppointmentBook() {
        Appointment app = new Appointment("07/15/2021 12:00","07/15/2021 13:00","Meeting with Barb");
        LinkedList appLL = new LinkedList();
        appLL.add(app);
        AppointmentBook book = new AppointmentBook("John",appLL);
        return book;
    }

}
