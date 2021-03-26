package com.epam.xmlparser;

import com.epam.xmlparser.entity.Node;
import com.epam.xmlparser.parser.XMLFileParser;
import com.epam.xmlparser.parser.XMLFileParserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String pathToXMLFile = "src/main/resources/notes.xml";
            XMLFileParser xmlFileParser = new XMLFileParser(pathToXMLFile);
            Node XMLHead = xmlFileParser.parseFile();

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(XMLHead);
            System.out.println(json);
        } catch (XMLFileParserException | JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }
}
