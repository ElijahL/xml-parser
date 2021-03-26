package com.epam.xmlparser.parser;

public class XMLFileParserException extends Exception {
    public XMLFileParserException() {
        super();
    }

    public XMLFileParserException(String message) {
        super(message);
    }

    public XMLFileParserException(Exception e) {
        super(e);
    }

    public XMLFileParserException(String message, Exception e){
        super(message, e);
    }
}
