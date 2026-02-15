import main.java.com.fatyk.taskmanager.menu.State;


void main() {
    State menuManager = State.MENU;

    System.out.println("Welcome to the Task manager!");

    while(!State.getExit()){
        menuManager.action();
        menuManager = State.getCurrent();
    }
}