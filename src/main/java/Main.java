public class Main {
    public static void main(String[] args) {
        // create IO class for input and output
        IO io = new IO();
        if (args.length == 0) {
            // lunch program in normal mode (console mode)
            io.startConsole();
        } else if (args.length == 2) {
            // lunch in testmode
            for (String arg : args) {
                // first argument must be a parameter "-i" referencing input
                if (arg.equals("-i")) {
                    // go to next argument
                    continue;
                } else if (arg.charAt(0) == '-') {
                    System.out.println("Input Error: false parameter (" + arg + ")");
                    return;
                }
                // second argument is input file with correct extension
                String[] inputFile = arg.split("\\.");
                if (inputFile.length < 2) {
                    System.out.println("Input Error: input file name is too short");
                    return;
                }
                // check file extension
                String extension = inputFile[inputFile.length - 1];
                if (!extension.equals(io.extension)) {
                    System.out.println("Input Error: wrong input file extension");
                    return;
                }
                // initiate files for input, output and errors
                io.initFiles(arg);

                // start reading the file
                boolean ok = io.readInputFile();
                if(!ok){
                    // abort process
                    return;
                }
//                io.submitError("test error");
                io.writeOutputFile();
                ok = io.commitErrorLog();
                if(!ok){
                    System.out.println("Error log could not be committed");
                }


            }
        } else {
            System.out.println("Input Error: false number of arguments");
            return;
        }
    }
}
