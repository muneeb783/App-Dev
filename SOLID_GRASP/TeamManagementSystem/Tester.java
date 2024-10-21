import java.util.logging.Logger;

public class Tester implements ITester {

    private String name;
    private String email;
    private static final Logger logger = Logger.getLogger(TeamManager.class.getName());

    public Tester(String name, String email) {
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

    @Override
    public void leaveProject(Project project) {
        project.removeTeamMember(this);
    }

    @Override
    public void reportBug(String bugReport) {
        logger.info(String.format("%s reported bug: %s", name, bugReport));
    }

    @Override
    public void receiveTestCase(String testCase) {
        logger.info(String.format("%s received test case: %s", name, testCase));
    }
}
