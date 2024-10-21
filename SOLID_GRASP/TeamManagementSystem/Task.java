package solid_grasp.team_management_system;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Task implements ITask {
    private String title;
    private String description;
    private int dueDate;
    private TaskStatus status;
    private int priority;
    private static final Logger logger = Logger.getLogger("Task Logger");

    public Task(String title, String description, int dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = (dueDate <= 31) ? dueDate : 31;
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
        logger.log(Level.INFO, () -> String.format("Executing Task - %s", this.getTitle()));
        if (this.status == TaskStatus.COMPLETE) {
            logger.log(Level.WARNING, "This task has already been completed.");
            return false;
        } else {
            this.setStatus(TaskStatus.COMPLETE);
            logger.log(Level.INFO, "Task Successfully Completed!");
            return true;
        }
    }
}
