package com.machucarro.entities;

/**
 * Created by Manuel on 28/05/2015.
 */
public class PhoneRecord implements Comparable<PhoneRecord>{
    private String name;
    private String familyName;
    private String phoneNumber;

    /** The Phone type like home, mobile, work...*/
    private String phoneType;

    public PhoneRecord(String name, String familyName, String phoneNumber, String phoneType) {
        this.name = name;
        this.familyName = familyName;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    @Override
    public int compareTo(PhoneRecord o) {
        final int EQUAL = 0;
        if(this == o) return EQUAL;
        if(this.equals(o)) return EQUAL;
        int result = this.getFamilyName().compareToIgnoreCase(o.getFamilyName());
        if(result == EQUAL) result = this.getName().compareToIgnoreCase(o.getName());
        if(result == EQUAL) result = this.getPhoneType().compareToIgnoreCase(o.getPhoneType());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneRecord)) return false;

        PhoneRecord that = (PhoneRecord) o;

        if (familyName != null ? !familyName.equals(that.familyName) : that.familyName != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (phoneType != null ? !phoneType.equals(that.phoneType) : that.phoneType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  "Name: " + name + '\n' +
                "Family Name: " + familyName + '\n' +
                "Phone Number: " + phoneNumber + '\n' +
                "Type: " + phoneType + '\n' +
                "======================================\n";
    }
}
