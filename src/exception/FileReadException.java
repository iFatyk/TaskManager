package exception;

public class FileReadException extends Exception{
    public FileReadException(String path, int row, String badWord)
    {
        super("Can't read field " + badWord + " in row " + row + " from file: " + path);
    }
}