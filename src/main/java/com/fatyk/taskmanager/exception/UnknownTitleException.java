package main.java.com.fatyk.taskmanager.exception;

public class UnknownTitleException extends Exception{
    public UnknownTitleException(String title){
        String message = "There is no task with title: " + title;
        super(message);
    }
}