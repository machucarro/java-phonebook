package com.machucarro.xml;

import com.machucarro.entities.PhoneRecord;

import java.io.File;
import java.util.SortedSet;

/**
 * Created by Manuel on 28/05/2015.
 */
public class PhoneBook {
    public static PhoneBook createWithFile(File phoneBookFile) {
        return null;
    }

    public void create(String name, String familyName, String phoneNumber, String phoneType) {

    }

    public SortedSet<PhoneRecord> find(String name, String familyName, String phoneType) {
        return null;
    }

    public int delete(String name, String familyName, String phoneType) {
        return 0;
    }

    public void doSave() {

    }
}
