package solid_grasp.team_management_system;
import java.util.logging.Logger;
public class TeamManager implements ITeamManager {

    private String name;
    private String email;
    private static final Logger logger = Logger.getLogger(TeamManager.class.getName());

    public TeamManager(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName(){
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
    public void assignTask(Task task, TeamMember member) {
        logger.info("Assigning task " + task.getTitle() + " to " + member.getName());
    }
}
