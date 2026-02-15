package main.java.com.fatyk.taskmanager.task;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Task implements Cloneable {
    private final int id;
    private final String title;
    private final String description;
    private Status status;
    private final Date createdAt;

    static public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    Task(int id, String title, String description, Status status){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = Date.from(Instant.now());
    }

    Task(int id, String title, String description, Status status, Date createdAt){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    int getId() {return id;}
    String getTitle() {return title;}
    String getDescription() {return description;}
    Status getStatus() {return status;}
    Date getCreationDate() {return createdAt;}

    void setStatus(Status status){
        this.status = status;
    }

    @Override
    public String toString()
    {
        return String.join("|", "" + id, title, description, status.toString(), dateFormat.format(createdAt));
    }

    @Override
    public int hashCode(){
        return 10 * id;
    }

    @Override
    public boolean equals(Object otherObject){
        if (this == otherObject) return true;

        if (!(otherObject instanceof Task task)) return false;

        return this.title.equals(task.title) & this.description.equals(task.description);
    }

    @Override
    public Task clone() throws CloneNotSupportedException{

        return (Task) super.clone();
    }
}