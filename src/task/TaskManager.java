package task;

import java.util.ArrayList;

import exception.UnknownIdException;
import exception.UnknownTitleException;

public class TaskManager{
    private ArrayList<Task> tasks;
    private int taskCounter;

    public TaskManager(){
        taskCounter = 0;
        tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks(){
        return (ArrayList<Task>) tasks.clone();
    }

    public void addTask(String title, String description, Status status) {
        tasks.add(new Task(++taskCounter, title, description, status));
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void changeStatus(int id, Status status) throws UnknownIdException{
        get(id).setStatus(status);
    }

    public void changeStatus(String title, Status status) throws UnknownTitleException{
        get(title).setStatus(status);
    }

    public Task get(int id) throws UnknownIdException{
        return tasks.get(searchById(id));
    }

    public Task get(String title) throws UnknownTitleException{
        return tasks.get(searchByTitle(title));
    }

    public void remove(int id) throws UnknownIdException{
        tasks.remove(searchById(id));
    }

    public void remove(String title) throws UnknownTitleException{
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