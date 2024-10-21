package SOLID_GRASP.TeamManagementSystem;
public interface ITeamMember {

    String getName();
    String getEmail();

    void joinProject(Project project);
    void leaveProject(Project project);
}
