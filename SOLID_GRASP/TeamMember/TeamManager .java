public class TeamManager implements ITeamManager {

    private String name;
    private String email;

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
        project.removeTeamMemeber(this);
    }

    @Override
    public void assignTask(Task task, TeamMember member) {
        System.out.println("Assigning task " + task.getTitle() + " to " + member.getName());
    }

    @Override
    public void reviewProgress(Project project) {
        System.out.println("Reviewing progress for project: " + project.getName());
    }

    @Override
    public void updateProjectStatus(Project project, String status) {
        project.setStatus(status);
        System.out.println("Project status updated to: " + status);
    }
}
