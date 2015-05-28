package com.machucarro.xml;

import com.machucarro.entities.PhoneRecord;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by Manuel on 28/05/2015.
 */
public class PhoneRecordParser {
    public static final String NAME_ATT = "name";
    public static final String FAMILY_NAME_ATT = "family-name";
    public static final String PHONE_TYPE_ATT = "phone-type";

    public static PhoneRecord parse(Node node) {
        PhoneRecord parsed = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String name = element.getAttribute(NAME_ATT);
            String familyName = element.getAttribute(FAMILY_NAME_ATT);
            String phoneType = element.getAttribute(PHONE_TYPE_ATT);
            String phone = element.getTextContent();
            parsed = new PhoneRecord(name, familyName, phone, phoneType);
        }
        return parsed;
    }

    public static void serializeInto(PhoneRecord sourceRecord, Element targetElement) {
        targetElement.setAttribute(NAME_ATT, sourceRecord.getName());
        targetElement.setAttribute(FAMILY_NAME_ATT, sourceRecord.getFamilyName());
        targetElement.setAttribute(PHONE_TYPE_ATT, sourceRecord.getPhoneType());
        targetElement.setTextContent(sourceRecord.getPhoneNumber());
    }
}
