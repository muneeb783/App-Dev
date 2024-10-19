import java.time.LocalDate;

public interface ITask {
    String getTitle(); // Returns the title of the class
    void setTitle(String title); // Sets title of class

    String getDescription(); // Returns the description of the class
    void setDescription(String description); // Sets description of class

    int getDueDate(); // Returns the due date of the class
    void setDueDate(int dueDate); // Sets due date of class

    TaskStatus getStatus(); // Returns the status of the class
    void setStatus(TaskStatus status); // Sets status of class

    int getPriority(); // Returns the priority of the class
    void setPriority(int priority); // Sets priority of class

    boolean execute(); // Returns the boolean representing if task was successfully executed
}
