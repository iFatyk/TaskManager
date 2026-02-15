package task;

import exception.UnknownStatusException;

public enum Status{
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String stringInterpretation;
    Status(String stringInterpretation)
    {
        this.stringInterpretation = stringInterpretation;
    }

    @Override
    public String toString(){
        return stringInterpretation;
    }

    static public Status parseStatus(String s) throws UnknownStatusException{
        if(s.equals(TODO.stringInterpretation)) return TODO;
        else if (s.equals(IN_PROGRESS.stringInterpretation)) return IN_PROGRESS;
        else if (s.equals(DONE.stringInterpretation)) return DONE;
        else throw new UnknownStatusException(s);
    }
}