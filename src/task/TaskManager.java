package task;

import java.util.ArrayList;

import exception.EmptyTitleException;
import exception.UnknownIdException;
import exception.UnknownTitleException;

class TaskManager{
    private ArrayList<Task> tasks;
    private int taskCounter;

    TaskManager(){
        taskCounter = 0;
        tasks = new ArrayList<>();
    }

    ArrayList<Task> GetTasks(){
        return (ArrayList<Task>) tasks.clone();
    }

    void addTask(String title, String description, Status status) throws EmptyTitleException {
        tasks.add(new Task(++taskCounter, title, description, status));
    }

    void changeStatus(int id, Status status) throws UnknownIdException{
        get(id).setStatus(status);
    }

    void changeStatus(String title, Status status) throws UnknownTitleException{
        get(title).setStatus(status);
    }

    Task get(int id) throws UnknownIdException{
        return tasks.get(searchById(id));
    }

    Task get(String title) throws UnknownTitleException{
        return tasks.get(searchByTitle(title));
    }

    void remove(int id) throws UnknownIdException{
        tasks.remove(searchById(id));
    }

    void remove(String title) throws UnknownTitleException{
        tasks.remove(searchByTitle(title));
    }

    private int searchById(int id) throws UnknownIdException {
        for(int i = 0; i < tasks.size(); i++){
            if (tasks.get(i).getId() == id) return i;
        }

        throw new UnknownIdException(id);
    }

    private int searchByTitle(String title) throws UnknownTitleException{
        for(int i = 0; i < tasks.size(); i++){
            if (tasks.get(i).getTitle().equals(title)) return i;
        }

        throw new UnknownTitleException(title);
    }
}