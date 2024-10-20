import java.time.LocalDate;

public class Task implements ITask {
    private String title;
    private String description;
    private int dueDate;
    private TaskStatus status;
    private int priority;

    public Task(String title, String description, int dueDate, int priority) {
        this.title = title;
        this.description = description;
        int currDays = LocalDate.now().getMonth().length(false);
        this.dueDate = (dueDate <= currDays) ? dueDate : currDays;
        this.status = TaskStatus.PENDING;
        this.priority = (priority <= 10 && priority >= 0) ? priority : 5;
    }

    public Task(String title, String description, int dueDate) {
        this(title, description, dueDate, 5);
    }


    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getDueDate() {
        return this.dueDate;
    }

    @Override
    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public TaskStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean execute() {
        System.out.println("Executing task: " + this.title);
        if (this.status == TaskStatus.COMPLETE) {
            System.out.println("This task has already been completed.");
            return false;
        } else {
            this.setStatus(TaskStatus.COMPLETE);
            System.out.println("Task Successfully Completed!");
            return true;
        }
    }
}
