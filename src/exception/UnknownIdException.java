package exception;

public class UnknownIdException extends Exception{
    public UnknownIdException(int id){
        String message = "There is no task with id: " + id;
        super(message);
    }
}