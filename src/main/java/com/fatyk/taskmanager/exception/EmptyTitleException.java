package main.java.com.fatyk.taskmanager.exception;

public class EmptyTitleException extends Exception {
    public EmptyTitleException(){
        super("Can't create task with empty title");
    }
}