package com.machucarro;

import com.machucarro.entities.PhoneRecord;
import com.machucarro.xml.PhoneBook;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.SortedSet;

/**
 * Main Class used for implementing the console user interface for the phone book
 */
public class PhoneBookMain {
    public static final String OPT_NEW = "1";
    public static final String OPT_FIND = "2";
    public static final String OPT_DELETE = "3";
    public static final String OPT_SAVE = "4";
    public static final String OPT_EXIT = "0";
    private static final String YES = "Y";
    private static final String NO = "N";

    private static String fileName = "phonebook.xml";
    private static PhoneBook phoneBook;
    private static Scanner keyboard;

    public static void main(String[] args) {
        //Check arguments
        if(args.length > 1){
            throw new RuntimeException("Please call the program with just one optional parameter which is the phonebook file name");
        } else if(args.length == 1){
            fileName = args[0];
        }

        //Open or create the file
        File phoneBookFile = new File(fileName);
        if(!phoneBookFile.exists()){
            try {
                phoneBookFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Could not create the given file: " + fileName);
            }
        }

        //Create a PhoneBook linked to the file
        try {
            phoneBook = PhoneBook.createWithFile(phoneBookFile);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Could not create a phone book associated to the given file: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException("Could not create a phone book because of problems with the given file: " + fileName);
        }

        keyboard = new Scanner(System.in);

        //Print the welcome
        doWelcome();
        boolean exit;
        do{
            exit = doMainMenu();
            System.out.println("Press enter to continue...");
            keyboard.nextLine();
        }while(!exit);
    }

    private static void doWelcome() {
        System.out.println("Welcome to the XML Phone Book Manager");
        System.out.println("The xml file to use is: " + fileName);
    }

    private static boolean doMainMenu() {
        boolean exit = false;
        System.out.println("What do you want to do next?");
        System.out.println(OPT_NEW + ".- Create a new phone record");
        System.out.println(OPT_FIND + ".- Search for phone records");
        System.out.println(OPT_DELETE + ".- Delete phone records");
        System.out.println(OPT_SAVE + ".- Save changes to file");
        System.out.println(OPT_EXIT + ".- Exit");
        String selected = keyboard.nextLine();
        if (selected.equals(OPT_NEW)) {
            doNew();
        } else if (selected.equals(OPT_FIND)) {
            doFind();
        } else if (selected.equals(OPT_DELETE)) {
            doDelete();
        } else if (selected.equals(OPT_SAVE)) {
            doSave();
        } else if (selected.equals(OPT_EXIT)) {
            exit = true;
            doExit();
        } else {
            System.out.println("Please choose a valid option");
        }
        return exit;
    }

    private static void doNew() {
        System.out.println("Creating a new phone record...");
        String familyName = doGetVariable("family name", true, keyboard);
        String name = doGetVariable("name", true, keyboard);
        String phoneNumber = doGetVariable("phone number", true, keyboard);
        String phoneType = doGetVariable("phone type", true, keyboard);
        phoneBook.create(name, familyName, phoneNumber, phoneType);
        System.out.println("Phone record created successfuly. Don't forget to save the changes to file.");
    }

    private static void doFind() {
        System.out.println("Searching for phone records...");
        String familyName = doGetVariable("family name", false, keyboard);
        String name = doGetVariable("name", false, keyboard);
        String phoneType = doGetVariable("phone type", false, keyboard);
        SortedSet<PhoneRecord> found = phoneBook.find(name, familyName, phoneType);
        if(!found.isEmpty()){
            for (PhoneRecord phoneRecord : found) {
                System.out.println(phoneRecord.toString());
            }
            System.out.println(found.size() + " phone records found!");
        }else{
            System.out.println("No phone records found for your search");
        }
    }

    private static void doDelete() {
        System.out.println("Deleting phone records...");
        String familyName = doGetVariable("family name", false, keyboard);
        String name = doGetVariable("name", false, keyboard);
        String phoneType = doGetVariable("phone type", false, keyboard);
        int deleted = phoneBook.delete(name, familyName, phoneType);
        System.out.println(deleted + " records deleted. Don't forget to save the changes to file.");
    }

    private static void doSave() {
        System.out.println("Saving changes to the file " + fileName + "...");
        boolean saved = phoneBook.save();
        if(saved){
            System.out.println("Changes saved successfully!");
        }else{
            System.out.println("An error occured when saving to the file!");
        }
    }

    private static void doExit() {
        String value;
        boolean validAnswer;
        do{
            System.out.println("Do you want to save the changes to the file before leaving? ("+ YES +"/" + NO + ")");
            value = keyboard.nextLine();
            validAnswer = YES.equalsIgnoreCase(value) || NO.equalsIgnoreCase(value);
        }while(!validAnswer);

        if(YES.equalsIgnoreCase(value)){
            doSave();
        }
    }

    private static String doGetVariable(String variableName, boolean required, Scanner keyboard) {
        String msg = "Please type in the " + variableName + " and press enter";
        if(!required){
            msg += " or leave it blank for any";
        }
        String value;
        do{
            System.out.println(msg);
            value = keyboard.nextLine().trim();
        }
        while(required && value.isEmpty());
        return value;
    }
}
