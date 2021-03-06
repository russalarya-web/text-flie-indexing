package com.indexing;

import java.io.*;
import java.util.*;

public class Main {

    // create list of commands
    public static void commands() {
        System.out.println("Commands:");
        System.out.println("quit: quits application");
        System.out.println("add: adds files or directories for indexing");
        System.out.println("rm: removes files or directories from indexing");
        System.out.println("read: displays contents of a given file or directory in the index");
        System.out.println("ls: displays the files and directories available in the index");
        System.out.println("search: searches for a word or phrase in the index");
        System.out.println("\ninfo: opens the readme file");
        System.out.println("\nNOTE: Commands are case-sensitive.");
    }

    // print when a command is invalid
    public static void invalidCommand(String command) {
        System.out.println(command + " is invalid.");
    }

    // readme file open
    public static void readme() throws FileNotFoundException {
        Scanner readme = new Scanner(new File("readme.txt"));

        while (readme.hasNext()) {
            System.out.println(readme.nextLine());
        }
    }

    // processes a file or directory
    public static File process(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            return file;
        }

        else {
            return null;
        }
    }

    // reads file
    public static void readFile(File file) throws FileNotFoundException {
        Scanner currentScanner = new Scanner(file);

        System.out.format("File: %s. Directory: %s%n", file.getName(), file.getParent());

        while (currentScanner.hasNext()) {
            System.out.println(currentScanner.nextLine());
        }

        System.out.print("\n");

        currentScanner.close();
    }

    // read directory
    public static void readDirectory(File directory) throws FileNotFoundException {
        File[] fileList = directory.listFiles();

        for (File subFile: Objects.requireNonNull(fileList)) {
            // for files
            if (subFile.isFile()) {
                readFile(subFile);
            }

            // for sub-directories
            else if (subFile.isDirectory()) {
                readDirectory(subFile);
            }
        }
    }

    // searches file for text
    public static void searchFile(String text, File file) throws FileNotFoundException {
        Scanner searchScanner = new Scanner(file);

        // counter to print only before first entry
        int count = 0;

        // print lines in which text is present
        while (searchScanner.hasNext()) {
            String currentLine = searchScanner.nextLine();

            if (currentLine.contains(text)) {
                // print for first entry only. done to avoid empty files taking up space.
                if (count == 0) {
                    System.out.format("File: %s. Directory: %s%n", file.getName(), file.getParent());
                }
                System.out.println(currentLine);
                count++;
            }
        }
        searchScanner.close();
    }

    // search directory for text
    public static void searchDirectory(String text, File directory) throws FileNotFoundException {
        File[] fileList = directory.listFiles();

        for (File subFile: Objects.requireNonNull(fileList)) {
            // for files
            if (subFile.isFile()) {
                searchFile(text, subFile);
            }

            // for sub-directories
            else if (subFile.isDirectory()) {
                searchDirectory(text, subFile);
            }
        }
    }

    // run application
    public static void run(InputStream inputStream) throws IOException {

        // set up input scanner
        Scanner sc = new Scanner(inputStream);

        String HELP = "Use the 'help' command for the list of commands available.";
        String MISSING_ERR = "Use the 'ls' command to check for files and directories in the index.";
        String SIZE_ERR = "Use the 'add' command to add files or directories.";

        // introductory text
        System.out.println("Welcome to text-file-indexing! A service for indexing text files.");

        // print list of commands
        commands();

        System.out.println(HELP);
        System.out.println("Go index!");

        // current state
        boolean running = true;

        // initialise new index
        Map<String, File> index = new HashMap<>();

        // run if state is running
        while(running) {
            // command input
            System.out.print("\n>> ");
            String command = sc.nextLine();

            // command preprocessing
            command = command.strip();

            // 'test' command
            if (command.contains("test")) {
                String fileName;
                String[] splitCommand = command.split(" ");

                if (splitCommand[0].equals("test")) {
                    StringBuilder temp = new StringBuilder();

                    // command line argument processing
                    if (splitCommand.length > 1) {
                        for (int i = 1; i < splitCommand.length; i++) {
                            temp.append(splitCommand[i]).append(" ");
                        }

                        fileName = temp.toString().strip();
                    }

                    // if argument is missing
                    else {
                        System.out.print("Enter testing file: ");
                        fileName = sc.nextLine();
                    }

                    // check if file exists
                    if (process(fileName) != null) {
                        InputStream testStream = new FileInputStream(process(fileName));
                        run(testStream);

                        running = false;
                    }

                    // error handling
                    else {
                        System.out.println(fileName + " does not exist.");
                    }


                }

                // invalid command check
                else {
                    invalidCommand(command);
                }
            }

            // 'quit' command
            else if (command.equals("quit")) {
                System.out.print("Ending application...");
                running = false;
            }

            // 'help' command
            else if (command.equals("help")) {
                commands();
            }

            // 'info' command
            else if (command.equals("info")) {
                readme();
            }

            // 'add' command
            else if (command.contains("add")) {
                String fileName;
                String[] splitCommand = command.split(" ");

                if (splitCommand[0].equals("add")) {
                    StringBuilder temp = new StringBuilder();

                    // command line argument processing
                    if (splitCommand.length > 1) {
                        for (int i = 1; i < splitCommand.length; i++) {
                            temp.append(splitCommand[i]).append(" ");
                        }

                        fileName = temp.toString().strip();
                    }

                    // if argument is missing
                    else {
                        System.out.print("Enter file name to add: ");
                        fileName = sc.nextLine();
                    }

                }

                // invalid command check
                else {
                    invalidCommand(command);
                    continue;
                }

                // check if file exists
                if (process(fileName) != null) {
                    index.put(fileName, process(fileName));
                    System.out.println("Successfully added " + fileName + ".");
                }

                // error handling
                else {
                    System.out.println(fileName + " does not exist.");
                }

            }

            // 'remove' command
            else if (command.contains("rm")) {
                String fileName;
                String[] splitCommand = command.split(" ");

                if (splitCommand[0].equals("rm")) {
                    StringBuilder temp = new StringBuilder();

                    // command line argument processing
                    if (splitCommand.length > 1) {
                        for (int i = 1; i < splitCommand.length; i++) {
                            temp.append(splitCommand[i]).append(" ");
                        }

                        fileName = temp.toString().strip();
                    }

                    // if argument is missing
                    else {
                        System.out.print("Enter file name to remove: ");
                        fileName = sc.nextLine();
                    }

                }

                // invalid command check
                else {
                    invalidCommand(command);
                    continue;
                }

                // check if file exists in index
                if (index.containsKey(fileName)) {
                    index.remove(fileName);
                    System.out.println("Successfully removed " + fileName + ".");
                }

                // error handling
                else {
                    System.out.println(fileName + " is not in the index.");
                    System.out.println(MISSING_ERR);
                }

            }

            // 'read' command
            else if (command.contains("read")) {
                String fileName;
                String[] splitCommand = command.split(" ");

                if (splitCommand[0].equals("read")) {
                    // check for empty index
                    if (index.size() > 0) {
                        StringBuilder temp = new StringBuilder();

                        // command line argument processing
                        if (splitCommand.length > 1) {
                            for (int i = 1; i < splitCommand.length; i++) {
                                temp.append(splitCommand[i]).append(" ");
                            }

                            fileName = temp.toString().strip();
                        }

                        // if argument is missing
                        else {
                            System.out.print("Enter file name to read: ");
                            fileName = sc.nextLine();
                        }
                    }

                    // empty index check
                    else {
                        System.out.println("There are no files or directories in the index.");
                        System.out.println(SIZE_ERR);

                        continue;
                    }

                }

                // invalid command check
                else {
                    invalidCommand(command);
                    continue;
                }

                // check if file exists in index
                if (index.containsKey(fileName)) {
                    File current = index.get(fileName);

                    // checking for directory
                    if (current.isDirectory()) {
                        readDirectory(current);
                    }

                    // file reading
                    else {
                        readFile(current);
                    }

                }

                // error handling
                else {
                    System.out.println(fileName + " is not in the index.");
                    System.out.println(MISSING_ERR);
                }
            }

            // 'list' command
            else if (command.equals("ls")) {

                // check for empty index
                if (index.size() > 0) {
                    for (String item: index.keySet()) {
                        System.out.println(item);
                    }
                }

                // error handling
                else {
                    System.out.println("There are no files or directories in the index.");
                    System.out.println(SIZE_ERR);
                }
            }

            // 'search' command
            else if (command.contains("search")) {
                String text;
                String[] splitCommand = command.split(" ");

                // check for empty index
                if (index.size() > 0) {

                    if (splitCommand[0].equals("search")) {
                        StringBuilder temp = new StringBuilder();

                        // command line argument processing
                        if (splitCommand.length > 1) {
                            for (int i = 1; i < splitCommand.length; i++) {
                                temp.append(splitCommand[i]).append(" ");
                            }

                            text = temp.toString().strip();
                        }

                        // if argument is missing
                        else {
                            System.out.print("Enter text to search: ");
                            text = sc.nextLine();
                        }

                    }

                    // invalid command check
                    else {
                        invalidCommand(command);
                        continue;
                    }

                    // searching index
                    for (String fileName: index.keySet()) {
                        File current = index.get(fileName);

                        // search directory
                        if (current.isDirectory()) {
                            searchDirectory(text, current);
                        }

                        // search file
                        else {
                            searchFile(text, current);
                        }
                    }
                }

                // error handling
                else {
                    System.out.println("There are no files or directories in the index.");
                    System.out.println(SIZE_ERR);
                }
            }

            // error handling
            else {
                invalidCommand(command);
                System.out.println(HELP);
            }
        }

        // close scanner
        sc.close();
    }

    // main application
    public static void main(String[] args) throws IOException {
        run(System.in);
    }
}