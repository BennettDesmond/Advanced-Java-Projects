package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextParser implements AppointmentBookParser{
    private String fileName;

    public TextParser() {
        fileName = "";
    }

    public TextParser(String fileName) {
        this.fileName = fileName;
    }

    public AppointmentBook parse() throws ParserException{
        try {
            BufferedReader parser = new BufferedReader(new FileReader(fileName));
            if(!parser.ready()) {
                throw new ParserException("Not able to read file");
            }
            return new AppointmentBook("John");
        } catch (IOException e) {
            throw new ParserException("Problem while parsing",e);
        }
    }

}
