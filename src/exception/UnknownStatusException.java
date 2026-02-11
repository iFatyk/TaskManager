package exception;

public class UnknownStatusException extends Exception{
    public String stringInterpretation;
    public UnknownStatusException(String status){
        stringInterpretation = status;
    }
}