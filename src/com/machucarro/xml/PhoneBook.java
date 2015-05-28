package com.machucarro.xml;

import com.machucarro.entities.PhoneRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * Created by Manuel on 28/05/2015.
 */
public class PhoneBook {
    public static final String PHONEBOOK_TAG = "phonebook";
    public static final String PHONE_RECORD_TAG = "phone";

    private Document document;
    private File file;

    /**
     * Private default constructor. Made like this in order to avoid exceptions on the constructor.
     */
    private PhoneBook(){}


    /**
     * Instantiator method which fills and returns an instance of PhoneBook linked to a given XML file.
     * @param phoneBookFile
     * @return
     * @throws ParserConfigurationException
     */
    public static PhoneBook createWithFile(File phoneBookFile) throws ParserConfigurationException, IOException {
        PhoneBook pb = new PhoneBook();
        pb.file = phoneBookFile;
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try {
            // Try to parse the file
            pb.document = documentBuilder.parse(phoneBookFile);
        } catch (SAXException e) {
            // If it fails, create a new DOM and initialise it
            pb.document = documentBuilder.newDocument();
            Element docElement = pb.document.createElement(PHONEBOOK_TAG);
            pb.document.appendChild(docElement);
        }
        return pb;
    }

    public void create(String name, String familyName, String phoneNumber, String phoneType) {
        PhoneRecord newRecord = new PhoneRecord(name, familyName, phoneNumber, phoneType);
        Element targetElement = this.document.createElement(PHONE_RECORD_TAG);
        PhoneRecordParser.serializeInto(newRecord, targetElement);
        this.document.getDocumentElement().appendChild(targetElement);
    }

    public SortedSet<PhoneRecord> find(String name, String familyName, String phoneType) {
        SortedSet<PhoneRecord> found = new TreeSet<PhoneRecord>();
        NodeList nodesFound = this.findNodes(name, familyName, phoneType);
        if(nodesFound == null){
            return found;
        }

        for (int i = 0; i < nodesFound.getLength(); i++) {
            Node n = nodesFound.item(i);
            found.add(PhoneRecordParser.parse(n));
        }
        return found;
    }

    public int delete(String name, String familyName, String phoneType) {
        NodeList nodesToDelete = this.findNodes(name, familyName, phoneType);
        int deleted = 0;
        if(nodesToDelete != null){
            for (int i = 0; i < nodesToDelete.getLength(); i++) {
                Node node = nodesToDelete.item(i);
                node.getParentNode().removeChild(node);
                deleted++;
            }
        }
        return deleted;
    }

    public boolean save() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult destination = new StreamResult(this.file);
            DOMSource source = new DOMSource(this.document);
            transformer.transform(source, destination);
        } catch (TransformerConfigurationException e) {
            return false;
        } catch (TransformerException e) {
            return false;
        }
        return true;
    }

    private NodeList findNodes(String name, String familyName, String phoneType){
        // The xPath Query to find the matching records: /phonebook/phone[@name='...' and @family-name='...' and type='...']
        String xPathQuery = "/" + PHONEBOOK_TAG + "/" + PHONE_RECORD_TAG;
        // The joiner is to dynamically construct the query part related to the parameters which are optional
        StringJoiner sj = new StringJoiner(" and ", "[", "]");
        sj.setEmptyValue("");
        if(name != null && !name.trim().isEmpty()){
            sj.add("@" + PhoneRecordParser.NAME_ATT + "='" + name + "'");
        }
        if(familyName != null && !familyName.trim().isEmpty()){
            sj.add("@" + PhoneRecordParser.FAMILY_NAME_ATT + "='" + familyName + "'");
        }
        if(phoneType != null && !phoneType.trim().isEmpty()){
            sj.add("@" + PhoneRecordParser.PHONE_TYPE_ATT + "='" + phoneType + "'");
        }
        xPathQuery += sj.toString();
        NodeList nodesFound;
        try {
            XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xPathQuery);
            nodesFound = (NodeList) expr.evaluate(this.document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            // If the expression is not well constructed we consider the search returns an empty set
            return null;
        }
        return nodesFound;
    }
}
