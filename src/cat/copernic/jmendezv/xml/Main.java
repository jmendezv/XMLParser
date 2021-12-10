package cat.copernic.jmendezv.xml;

import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.*;



public class Main {

    static void loadXml() {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            // Obtain DocumentBuilder factory
            df = DocumentBuilderFactory.newInstance();

            // Get DocumentBuilder instance from factory
            builder = df.newDocumentBuilder();

            // Document object instance now is the in-memory representation of the XML file
            document = builder.parse("students.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void listIds() {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            df = DocumentBuilderFactory.newInstance();
            builder = df.newDocumentBuilder();
            document = builder.parse("resources/students.xml");

            // Query by tag name
            NodeList studentNodesList = document.getElementsByTagName("student");

            for (int i = 0; i < studentNodesList.getLength(); i++) {
                Element studentItem = (Element) studentNodesList.item(i);
                System.out.println(studentItem.getAttribute("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void listXml() {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            df = DocumentBuilderFactory.newInstance();
            builder = df.newDocumentBuilder();
            document = builder.parse("resources/students.xml");

            // Query by tag name
            NodeList studentNodesList = document.getElementsByTagName("student");

            for (int i = 0; i < studentNodesList.getLength(); i++) {
                Element studentItem = (Element) studentNodesList.item(i);
                System.out.println(studentItem.getAttribute("id"));

                NodeList studentItemChildNodes = studentItem.getChildNodes();
                for (int j = 0; j < studentItemChildNodes.getLength(); j++) {
                    Node childNode = studentItemChildNodes.item(j);

                    // Element
                    if (childNode.getNodeType() == Node.ELEMENT_NODE && "name".equals(childNode.getNodeName())) {
                        System.out.println("name: " + childNode.getFirstChild().getNodeValue());
                    }

                    // Element
                    if (childNode.getNodeType() == Node.ELEMENT_NODE && "gender".equals(childNode.getNodeName())) {
                        System.out.println("gender: " + childNode.getFirstChild().getNodeValue());
                    }

                    // Comment
                    if (childNode.getNodeType() == Node.COMMENT_NODE) {
                        Comment comment = (Comment) childNode;
                        System.out.println("comment: " + comment.getData());
                    }

                    // CharacterData (CDATA)
                    if (childNode.getNodeType() == Node.CDATA_SECTION_NODE) {
                        CharacterData cData = (CharacterData) childNode;
                        System.out.println("CDATA: " + cData.getData());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void transformXml() {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            df = DocumentBuilderFactory.newInstance();
            builder = df.newDocumentBuilder();
            document = builder.parse("resources/students.xml");

            Node firstGenderNode = document.getElementsByTagName("gender").item(0);

            // Remove self from parent
            firstGenderNode.getParentNode().removeChild(firstGenderNode);

            TransformerFactory tfFactory = TransformerFactory.newInstance();
            Transformer tf = tfFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(
                    new FileOutputStream("students_modified.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addElement() {
        DocumentBuilderFactory df;
        DocumentBuilder builder;
        Document document;

        try {
            df = DocumentBuilderFactory.newInstance();
            builder = df.newDocumentBuilder();
            document = builder.parse("resources/students.xml");

            // New name element
            Element name = document.createElement("name");
            name.setTextContent("Bugs Bunny");

            // New gender element
            Element gender = document.createElement("gender");
            gender.setTextContent("male");

            // New student element
            Element newStudent = document.createElement("student");
            // Add id attribute
            newStudent.setAttribute("id", "004");

            newStudent.appendChild(name);
            newStudent.appendChild(gender);

            // Obtain the first student element
            Element firstStudent = (Element) document.getElementsByTagName("student").item(0);

            // Insert the new created element before the first student element
            firstStudent.getParentNode().insertBefore(newStudent, firstStudent);

            TransformerFactory tfFactory = TransformerFactory.newInstance();
            Transformer tf = tfFactory.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(document), new StreamResult(
                    new FileOutputStream("students_modified.xml")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void fromXmlToJson() {
        String xml= "<?xml version=\"1.0\" ?><root><test       attribute=\"text1\">javatpoint</test><test attribute=\"text2\">JTP</test></root>";
        try {
            JSONObject json = XML.toJSONObject(xml);
            String jsonString = json.toString(4);
            System.out.println(jsonString);

        }catch (JSONException e) {
            System.out.println(e.toString());
        }
    }

    static void fromXmlToJson(String filename) {


        try {
            JSONObject json = XML.toJSONObject(Files.readString(Paths.get(filename)));
            String jsonString = json.toString(4);
            System.out.println(jsonString);

        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {

        //loadXml();
        //listIds();
        //listXml();
        //transformXml();
        //addElement();
        fromXmlToJson("resources/students.xml");
    }
}
