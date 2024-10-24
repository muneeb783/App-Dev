package SOLID_GRASP.TeamManagementSystem;
public interface ITeamManager extends ITeamMember {

    void assignTask(Task task, TeamMember member);
}
