import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Input Output class for main process handling
 */
public class IO implements InputOutput {

    public Scanner sc = new Scanner(System.in); // for user input via console
    // restrictions
    public String extension = "txt";
    public String lineCommentChar = "%";
    public String blockCommentCharBegin = "%*";
    public String blockCommentCharEnd = "*%";
    public int maxInputFileLength = 1000;
    public String mainDelimiter = ",";  // delimiter for values
    public String secondDelimiter = "\t";  // delimiter for value pares


    // files options
    public String inputFilePath;
    public String outputFilePath;
    public String errorFilePath;
    // files
    private File inputFile;

    // saving errors
    public String errorLog = "";

    // problem specific values
    // saving first comment
    public String firstComment = "";
    private int gridSize = 20;
    private int timeLength = 10;

    /**
     * console options in an ArrayList
     */
    private ArrayList<String> menuOptions = new ArrayList<String>() {
        {
            add("0");
        }
    };
    // program name to use in console
    private String programName = "Find Minimal Road v0.1";


    /**
     * Reads input file according restrictions
     */
    public boolean readInputFile() {
        try {
            if (!this.inputFile.exists()) {
                submitError("Input File don't exist");
                return false;
            } else if (this.inputFile.length() == 0) {
                submitError("Input File is empty");
                return false;
            }
            BufferedReader reader = new BufferedReader(new FileReader(this.inputFile));
            StringBuilder fileContents = new StringBuilder();
            char[] buffer = new char[4096];
            while (reader.read(buffer, 0, 4096) > 0) {
                fileContents.append(buffer);
            }
            String input = fileContents.toString();
            input = stripBlockComments(this.blockCommentCharBegin, this.blockCommentCharEnd, input);
            // strip line comment
            input = stripLineComments(this.lineCommentChar, input);
            // check if input has s and z
            if (!(input.contains("z") || input.contains("Z")) || !(input.contains("s") || input.contains("S"))) {
                submitError("Kein Start oder Zielzeichen vorhanden");
                return false;
            }
            String[] inputLines = input.split(System.getProperty("line.separator"));
            for (int i = 0; i < inputLines.length; i++) {
                String line = inputLines[i];
                if(line.length() == 0){
                    continue;
                }
                String[] values = line.replaceAll("\\s+","").split(this.mainDelimiter);
                // check size of the grid
                if(values.length > this.gridSize){
                    submitError("Feldgroesse stimmt nicht");
                    return false;
                }
                // parse to number
                int j = 0;
                for(String value:values){
                    try{
                        int time = Integer.parseInt(value);

                    }
                    catch(NumberFormatException nfe){
                        submitError("Ein Wert ist keine Zahl");
                        return false;
                    }
                }


            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            submitError(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Removes all line comments from a string
     * @param token line comment character(s)
     * @param input string to be filtered
     * @return filtered string from line comments
     */
    private String stripLineComments(String token, String input) {
        StringBuilder output = new StringBuilder();
        String[] inputLines = input.split("\n");
        for (int i = 0; i < inputLines.length; i++) {
            String line = inputLines[i];
            // save first line comment if exists
            if (i == 0) {
                if (line.contains(token)) {
                    this.firstComment = line.substring(line.indexOf(token) + 1, line.length() - 1);
                } else {
                    submitError("First line don't have a comment");
                }
            }
            line = line.split(token)[0];
            if (line.length() != 0) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * Removes all block comments from a string
     * @param beginToken block comment begin character(s)
     * @param endToken block comment begin character(s)
     * @param input string to be filtered
     * @return filtered string from block comments
     */
    private String stripBlockComments(String beginToken, String endToken, String input) {
        StringBuilder output = new StringBuilder();
        while (true) {
            // search one occurrence at a time
            int begin = input.indexOf(beginToken);
            int end = input.indexOf(endToken, begin + beginToken.length());
            if (begin == -1 || end == -1) {  // if no occurrences where found
                output.append(input);
                return output.toString();
            }
            output.append(input.substring(0, begin));
            input = input.substring(end + endToken.length());
        }
    }

    /**
     * Write ouput file according to predefined rules
     */
    public void writeOutputFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFilePath));
            String str = "test output";
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            submitError(e.getMessage());
        }
    }

    /**
     * Initiates the input, output and error file path variables according to given input file name
     *
     * @param inputFilePath: String
     */
    public void initFiles(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        this.inputFile = new File(this.inputFilePath);
        // get error file name
        String name = inputFile.getName().split("\\.")[0];
        if (this.inputFilePath.contains(File.separator)) {
            this.errorFilePath = this.inputFilePath.substring(0, this.inputFilePath.lastIndexOf(File.separator) + 1) + name
                    + ".error." + this.extension;
        } else {
            // if same directory
            this.errorFilePath += ".error." + this.extension;
        }
        // get output file name
        name = inputFile.getName().split("\\.")[0];
        if (this.inputFilePath.contains(File.separator)) {
            this.outputFilePath = this.inputFilePath.substring(0, this.inputFilePath.lastIndexOf(File.separator) + 1) + name
                    + ".output." + this.extension;
        } else {
            this.outputFilePath += ".output." + this.extension;
        }


    }

    /**
     * appends the error message to the overall error log of the programm with a time stamp at the beginning
     *
     * @param error: String
     */
    public void submitError(String error) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.errorLog += dtf.format(now) + ": " + error + "\n";
    }

    /**
     * Write the error log to a file with error log file path.
     *
     * @return ture: if successful/false: if unsuccessful
     */
    public boolean commitErrorLog() {
        try {
            // Files.write(Paths.get(this.errorFilePath), this.errorLog.getBytes());
            RandomAccessFile stream = new RandomAccessFile(this.errorFilePath, "rw");
            FileChannel channel = stream.getChannel();
            FileLock lock = null;

            String value = this.errorLog;
            byte[] strBytes = value.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
            buffer.put(strBytes);
            buffer.flip();
            channel.write(buffer);
            stream.close();
            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Starts the console mode of the programm
     */
    public void startConsole() {
        System.out.println("Program" + this.programName + "started...");
        while (true) {
            printMenu();
            boolean exit = scanMenu();
            if (exit) {
                System.out.println("Program " + this.programName + "exited");
                break;
            }
        }
    }

    /**
     * Print the main menu on the console
     */
    public void printMenu() {
        System.out.println("Main Menu:");
        System.out.println("(0) exit");
    }

    /**
     * Get user input option for the main menu
     *
     * @return true: if program should exit/false: if repeat console input procedure
     */
    public boolean scanMenu() {
        String key = sc.nextLine();
        int option;
        if (!this.menuOptions.contains(key)) {
            option = -1;
        } else {
            option = Integer.parseInt(key);
        }
        switch (option) {
            case 0:
                // exit program
                return true;
            default:
                System.out.println("Wrong input!");
                return false;
        }
    }

}
