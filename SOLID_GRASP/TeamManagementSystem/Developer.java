package SOLID_GRASP.TeamManagementSystem;
import java.util.logging.Logger;
import java.util.logging.Level;
public class Developer implements IDeveloper {

    private String name;
    private String email;
    private static final Logger logger = Logger.getLogger(TeamManager.class.getName());

    public Developer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void joinProject(Project project) {
        project.addTeamMember(this);
    }

    public void leaveProject(Project project) {
        project.removeTeamMember(this);
    }

    @Override
    public void receiveTask(Task task) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("%s received task: %s", name, task.getTitle()));
        }
    }
    @Override
    public void completeTask(Task task) {
        task.execute();
    }
}
