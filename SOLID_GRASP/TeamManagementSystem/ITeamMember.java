package solid_grasp.team_management_system;
public interface ITeamMember {

    String getName();
    String getEmail();

    void joinProject(Project project);
    void leaveProject(Project project);
}
