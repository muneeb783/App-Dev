package solid_grasp.team_management_system;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecurringTask extends Task {
    private String recurrencePattern;
    private static final Logger logger = Logger.getLogger("Task Logger");

    public RecurringTask(String title, String description, int dueDate, int priority, String recurrencePattern) {
        super(title, description, dueDate, priority);
        this.recurrencePattern = recurrencePattern;
    }

    public RecurringTask(String title, String description, int dueDate, String recurrencePattern) {
        super(title, description, dueDate);
        this.recurrencePattern = recurrencePattern;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    @Override
    public boolean execute() {
        logger.log(Level.INFO, () -> String.format("Executing Recurring Task - %s", this.getTitle()));
        if (super.execute()) {
            updateDueDate();
            logger.log(Level.INFO, () -> String.format("Next occurrence scheduled on day: %s", this.getDueDate()));
            return true;
        }
        return false;
    }

    private void updateDueDate() {
        switch (recurrencePattern.toLowerCase()) {
            case "daily":
                setDueDate((getDueDate() % 31) + 1); // Move to next day
                break;
            case "weekly":
                setDueDate(((getDueDate() + 6) % 31) + 1); // Move 7 days ahead
                break;
            case "monthly":
                // to keep it simple, same due date... subject to change
                break;
            default:
                logger.log(Level.WARNING, "Unknown recurrence pattern.");
                break;
        }
    }
}