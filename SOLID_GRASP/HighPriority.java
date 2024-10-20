import java.util.logging.Level;
import java.util.logging.Logger;

public class HighPriority extends Task {
    private boolean urgent;
    private static final Logger logger = Logger.getLogger("HighPriorityTask Logger");

    public HighPriority(String title, String description, int dueDate) {
        super(title, description, dueDate, 10); //max due date of 10 days
        this.urgent = true;
        notifyTeamMembers();
    }

    public boolean isUrgent() {
        return urgent;
    }

    private void notifyTeamMembers() {
        logger.log(Level.INFO, "Urgent task created: " + getTitle() + ". Notifying team members...");
    }

    @Override
    public void setPriority(int priority) {
        super.setPriority(10);
        logger.log(Level.WARNING, "Priority of HighPriorityTask cannot be changed. It remains at maximum.");
    }

}