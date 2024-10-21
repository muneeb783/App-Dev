package solid_grasp.team_management_system;
public interface ITeamManager extends ITeamMember {

    void assignTask(Task task, TeamMember member);
}
