package com.machucarro;

import com.machucarro.entities.PhoneRecord;
import com.machucarro.xml.PhoneBook;

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
                throw new RuntimeException("Could not create the specified file: " + fileName);
            }
        }

        //Create a PhoneBook linked to the file
        phoneBook = PhoneBook.createWithFile(phoneBookFile);

        keyboard = new Scanner(System.in);

        //Print the welcome
        doWelcome(fileName);
        boolean exit;
        do{
            exit = doMainMenu(keyboard, phoneBook);
            System.out.println("Press enter to continue...");
            keyboard.nextLine();
        }while(!exit);
    }

    private static void doWelcome(String fileName) {
        System.out.println("Welcome to the XML Phone Book Manager");
        System.out.println("The xml file to use is: " + fileName);
    }

    private static boolean doMainMenu(Scanner keyboard, PhoneBook phoneBook) {
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
        String familyName = doGetVariable("family name", true, keyboard);
        String name = doGetVariable("name", true, keyboard);
        String phoneType = doGetVariable("phone type", true, keyboard);
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
        String familyName = doGetVariable("family name", true, keyboard);
        String name = doGetVariable("name", true, keyboard);
        String phoneType = doGetVariable("phone type", true, keyboard);
        int deleted = phoneBook.delete(name, familyName, phoneType);
        System.out.println(deleted + " records deleted. Don't forget to save the changes to file.");
    }

    private static void doSave() {
        System.out.println("Saving changes to the file " + fileName + "...");
        phoneBook.doSave();
        System.out.println("Changes saved successfully!");
    }

    private static void doExit() {

    }

    private static String doGetVariable(String variableName, boolean required, Scanner keyboard) {
        String value = "";
        String msg = "Please type in the " + variableName + " and press enter";
        if(!required){
            msg += " or leave it blank for any";
        }
        do{
            System.out.println(msg);
            value = keyboard.nextLine().trim();
        }
        while(required && value.isEmpty());
        return value;
    }
}
