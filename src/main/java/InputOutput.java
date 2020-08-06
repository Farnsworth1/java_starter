public interface InputOutput {
    boolean readInputFile();
    void writeOutputFile();
    void submitError(String error);
    boolean commitErrorLog();
    void startConsole();
}
