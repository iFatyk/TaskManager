package main.java.com.fatyk.taskmanager.task;

import main.java.com.fatyk.taskmanager.exception.EmptyTitleException;
import main.java.com.fatyk.taskmanager.exception.FileReadException;
import main.java.com.fatyk.taskmanager.exception.UnknownStatusException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class TaskFileRepository{
    public TaskFileRepository(){}

    public void SaveTasks(ArrayList<Task> tasks, String path) throws IOException {
        try(FileWriter writer = new FileWriter(path, false)) {
            for (Task task : tasks) {
                writer.write(task.toString());
            }
            writer.flush();
        }
    }

    public ArrayList<Task> ReadTasks(String path) throws IOException, FileReadException, EmptyTitleException {
        StringBuilder line = new StringBuilder();
        int c;

        ArrayList<Task> tasks = new ArrayList<>();

        try(FileReader reader = new FileReader(path)){
            while((c = reader.read()) != -1){
                line.append((char) c);
            }
        }

        String[] stringTasks = line.toString().split("\n");

        for(int i = 0; i < stringTasks.length; i++){
            String stringTask = stringTasks[i];
            String[] taskParameters = stringTask.split("\\|");

            int id = Integer.parseInt(taskParameters[0]);
            String title = taskParameters[1];
            String description = taskParameters[2];

            Status status;
            try {
                status = Status.parseStatus(taskParameters[3]);
            }
            catch (UnknownStatusException exc){
                throw new FileReadException(path, i + 1, exc.getMessage());
            }

            Date createdAt;
            try{
                createdAt = Task.dateFormat.parse(taskParameters[4]);
            }
            catch (ParseException parseExc){
                throw new FileReadException(path, i + 1, taskParameters[4]);
            }

            tasks.add(new Task(id, title, description, status, createdAt));
        }

        return tasks;
    }
}