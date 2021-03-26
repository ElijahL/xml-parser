package com.epam.xmlparser.parser;

import com.epam.xmlparser.entity.Attribute;
import com.epam.xmlparser.entity.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XMLFileParser {
    private File XMLFile;

    public XMLFileParser(String pathToXMLFile) {
        XMLFile = new File(pathToXMLFile);
    }

    private Stream<String> readAndPrepareFileForParsing() throws XMLFileParserException {
        String preparedDataString;
        try(BufferedReader reader = new BufferedReader(new FileReader(XMLFile))){
            StringBuilder stringBuilder = new StringBuilder();
            String nextLine;
            while((nextLine = reader.readLine()) != null){
                    stringBuilder.append(nextLine);
            }
            preparedDataString = stringBuilder.toString()
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace("<", "\n<")
                    .replace(">", ">\n");
        } catch (IOException e) {
            throw new XMLFileParserException("We got problems in reading file: " + e.getMessage());
        }

        return preparedDataString.lines().filter(line -> !line.isBlank());
    }

    public Node parseFile() throws XMLFileParserException {
        Vector<Node> nodes = new Vector<>();

        List<String> lines = readAndPrepareFileForParsing().collect(Collectors.toList());
        for (String str : lines) {
            if (!str.contains("<?")) {
                if (str.contains("</")) {
                    if (nodes.size() > 1) {
                        Node tmp = nodes.lastElement();
                        nodes.remove(tmp);
                        nodes.lastElement().addNode(tmp);
                    }
                } else if (str.contains("<")) {
                    nodes.add(new Node());
                    Pattern pattern = Pattern.compile("[^<\\s\\t][^\\s\\t>]+");
                    Matcher matcher = pattern.matcher(str);
                    matcher.find();
                    String name = matcher.group();
                    nodes.lastElement().setName(name);
                    List<String> attrs = new ArrayList<>();
                    pattern = Pattern.compile("\\S+=\"[^\"]*\"");
                    matcher = pattern.matcher(str);
                    while (matcher.find()) {
                        attrs.add(matcher.group());
                    }
                    nodes.lastElement().setAttributes(createAttributes(attrs));
                } else {
                    nodes.lastElement().setContent(str);
                }
            }
        }
        return nodes.lastElement();
    }

    private List<Attribute> createAttributes(List<String> lines) {
        List<Attribute> attributes = new ArrayList<>();
        for (String str : lines) {
            Pattern pattern = Pattern.compile("[^\"=]+");
            Matcher matcher = pattern.matcher(str);
            matcher.find();
            String name = matcher.group();
            matcher.find();
            String value = matcher.group();
            attributes.add(new Attribute(name, value));
        }
        return attributes;
    }
}
