package menu;

import java.io.IOException;
import java.util.Scanner;

import exception.*;
import task.Status;
import task.Task;
import task.TaskFileRepository;
import task.TaskManager;


public enum State{
    MENU{
        @Override
        public void action(){
            State.clearCls();

            System.out.print(
                    """
                            Choose the action:
                    
                                1. Create new task
                                2. Show all available tasks
                                3. Change task status
                                4. Delete task
                                5. Load tasks from file
                                6. Save tasks to file
                                0. Exit
                    """
            );
            System.out.print("Action: ");
            changeState(State.scan.nextLine());
            State.printErrorMessage();
        }

        @Override
        public void changeState(String input){
            //Мб будут траблы с ссылками в previous и current, надо будет делать clone
            switch (input){
                case "1" -> State.change(ADDING_TASK);
                case "2" -> State.change(SHOW_ALL);
                case "3" -> State.change(CHANGE_STATUS);
                case "4" -> State.change(DELETE_TASK);
                case "5" -> State.change(LOAD_TASKS);
                case "6" -> State.change(SAVE_TASKS);
                case "0" -> State.needExit = true;
                default -> State.errorMessage = "You can input only 1-6 and 0.";
            }
        }
    },
    ADDING_TASK{
        @Override
        public void action(){
            State.clearCls();

            System.out.println("Creating new task.");
            changeState("");
        }

        @Override
        public void changeState(String input) {
            //Мб будут траблы с ссылками в previous и current, надо будет делать clone
            change(INPUT_TITLE);
        }
    },
    INPUT_TITLE{
        @Override
        public void action(){
            System.out.print("Input task title: ");
            taskTitle = scan.nextLine();
            System.out.println();

            changeState("");
        }

        @Override
        public void changeState(String input) {
            switch (previous){
                case ADDING_TASK -> State.change(INPUT_DESCRIPTION);
                case CHANGE_STATUS -> State.change(INPUT_STATUS);
                case DELETE_TASK -> State.change(DELETING_TASK);
            }
        }
    },
    INPUT_ID{
        @Override
        public void action(){
            System.out.print("Input task id: ");
            try {
                taskId = Integer.parseInt(scan.nextLine());
            }
            catch(NumberFormatException ex){
                errorMessage = "You can input only task id (integer number).";
            }
            System.out.println();

            if (errorMessage.isEmpty()) changeState("");
            else State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            switch (previous){
                case ADDING_TASK -> State.change(INPUT_DESCRIPTION);
                case CHANGE_STATUS -> State.change(INPUT_STATUS);
                case DELETE_TASK -> State.change(DELETING_TASK);
            }
        }
    },
    INPUT_DESCRIPTION{
        @Override
        public void action(){
            System.out.print("Input task description: ");
            taskDescription = scan.nextLine();
            System.out.println();

            changeState("");
        }

        @Override
        public void changeState(String input) {
            State.change(INPUT_STATUS);
        }
    },
    INPUT_STATUS{
        @Override
        public void action() {
            System.out.print("Input task status (TODO, IN_PROGRESS, DONE): ");
            try {
                taskStatus = Status.parseStatus(scan.nextLine());
            } catch (UnknownStatusException ex) {
                errorMessage = "You can input only TODO, IN_PROGRESS, DONE. Try again.";
            }

            if (errorMessage.isEmpty()) changeState("");
            else State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            switch (previous){
                case INPUT_DESCRIPTION -> State.change(CREATING_NEW_TASK);
                case INPUT_ID, INPUT_TITLE -> State.change(CHANGING_TASK_STATUS);
            }
        }
    },
    CHANGING_TASK_STATUS{
        @Override
        public void action() {
            try {
                if (!taskTitle.isEmpty()) taskManager.changeStatus(taskTitle, taskStatus);
                else taskManager.changeStatus(taskId, taskStatus);
            }
            catch (UnknownTitleException uTtlEx){
                errorMessage = "Can't find task with such title.";
            }
            catch (UnknownIdException uIdEx){
                errorMessage = "Can't find task with such id.";
            }

            if (errorMessage.isEmpty()) changeState("");
            else State.printErrorMessage();

            taskId = 0;
            taskTitle = "";
            taskStatus = Status.TODO;
        }

        @Override
        public void changeState(String input) {
            State.change(MENU);
        }
    },
    CREATING_NEW_TASK{
        @Override
        public void action() {
            taskManager.addTask(taskTitle, taskDescription, taskStatus);
            taskTitle = "";
            taskDescription = "";
            taskStatus = Status.TODO;

            changeState("");
        }

        @Override
        public void changeState(String input) {
            State.change(MENU);
        }
    },
    SHOW_ALL{
        @Override
        public void action() {
            State.clearCls();

            for(Task task : taskManager.getTasks()) {
                System.out.println(task.toString());
            }

            System.out.print("\n0. Back\n");
            changeState(State.scan.nextLine());
            State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            switch (input) {
                case "0" -> State.change(previous);
                default -> errorMessage = "You can only print \"0\"";
            }
        }
    },
    CHANGE_STATUS{
        @Override
        public void action() {
            State.clearCls();

            System.out.println("Changing task status\n");

            System.out.println("""
                            Choose task for changing. Input task id or title:

                            1. Id
                            2. Title
                            0. Back
                    """
            );

            changeState(State.scan.nextLine());
            State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            switch (input) {
                case "1" -> State.change(INPUT_ID);
                case "2" -> State.change(INPUT_TITLE);
                default -> errorMessage = "You can only print 1, 2 or 0";
            }
        }
    },
    DELETE_TASK{
        @Override
        public void action() {
            State.clearCls();

            System.out.println("Deleting task\n");

            System.out.println("""
                            Choose task for deleting. Input task id or title:

                            1. Id
                            2. Title
                            0. Back
                    """
            );

            changeState(State.scan.nextLine());
            State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            switch (input) {
                case "1" -> State.change(INPUT_ID);
                case "2" -> State.change(INPUT_TITLE);
                default -> errorMessage = "You can only print 1, 2 or 0";
            }
        }
    },
    DELETING_TASK{
        @Override
        public void action() {
            try {
                if (!taskTitle.isEmpty()) taskManager.remove(taskTitle);
                else taskManager.remove(taskId);
            }
            catch (UnknownTitleException uTtlEx){
                errorMessage = "Can't find task with such title.";
            }
            catch (UnknownIdException uIdEx){
                errorMessage = "Can't find task with such id.";
            }

            if (errorMessage.isEmpty()) changeState("");
            else State.printErrorMessage();

            taskId = 0;
            taskTitle = "";
            taskStatus = Status.TODO;
        }

        @Override
        public void changeState(String input) {
            State.change(MENU);
        }
    },
    INPUT_FILE_PATH{
        @Override
        public void action() {
            System.out.print("Input file path (in format: name.txt): ");
            tasksFilePath = scan.nextLine();

            changeState(tasksFilePath);
        }

        @Override
        public void changeState(String input) {
            if(input.equals("0")) State.change(MENU);
            else {
                switch (previous) {
                    case LOAD_TASKS -> State.change(LOADING_TASKS_FROM_FILE);
                    case SAVE_TASKS -> State.change(SAVING_TASK_TO_FILE);
                }
            }
        }
    },
    LOAD_TASKS{
        @Override
        public void action() {
            State.clearCls();

            System.out.println("Loading tasks from file.\n");

            changeState("");
        }

        @Override
        public void changeState(String input) {
            State.change(INPUT_FILE_PATH);
        }
    },
    LOADING_TASKS_FROM_FILE{
        @Override
        public void action(){
            try {
                TaskFileRepository reader = new TaskFileRepository();
                for(Task task : reader.ReadTasks(tasksFilePath)){
                    taskManager.addTask(task);
                }
            }
            catch (IOException io){
                errorMessage = "Have some problems with reading file:\n" + io.getMessage();
            }
            catch (FileReadException rEx){
                errorMessage = "Can't read file rightly:\n" + rEx.getMessage();
            }
            catch (EmptyTitleException ttlEx){
                errorMessage = "Can't find title.";
            }

            changeState("");
            State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            if(errorMessage.isEmpty()) State.change(MENU);
            else State.change(INPUT_FILE_PATH);
        }
    },
    SAVE_TASKS{
        @Override
        public void action() {
            State.clearCls();

            System.out.println("Saving tasks to file.\n");

            changeState("");
        }

        @Override
        public void changeState(String input) {
            State.change(INPUT_FILE_PATH);
        }
    },
    SAVING_TASK_TO_FILE {
        @Override
        public void action(){
            try {
                TaskFileRepository writer = new TaskFileRepository();
                writer.SaveTasks(taskManager.getTasks(), tasksFilePath);
            }
            catch (IOException io){
                errorMessage = "Have some problems with reading file:\n" + io.getMessage();
            }

            changeState("");
            State.printErrorMessage();
        }

        @Override
        public void changeState(String input) {
            if(errorMessage.isEmpty()) State.change(MENU);
            else State.change(INPUT_FILE_PATH);
        }
    };

    private static final TaskManager taskManager;
    private static String taskTitle;
    private static String taskDescription;
    private static Status taskStatus;
    private static int taskId;
    private static String tasksFilePath;

    private static State current;
    private static State previous;
    private static boolean needExit;
    private static final Scanner scan = new Scanner(System.in);
    private static String errorMessage;
    static{
        current = MENU;
        previous = null;
        needExit = false;
        errorMessage = "";
        taskManager = new TaskManager();
        taskTitle = "";
        taskDescription = "";
        taskStatus = Status.TODO;
    }

    private static void change(State newState){
        previous = current;
        current = newState;
    }

    private static void printErrorMessage(){
        if(!errorMessage.isEmpty()){
            System.out.println(errorMessage);
            errorMessage = "";
        }
    }

    private static void clearCls(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static boolean getExit() {return needExit;}
    public static State getCurrent(){return current;}
    public abstract void changeState(String input);
    public abstract void action();
}