package solid_grasp.team_management_system;
public interface IDeveloper extends ITeamMember {



    void receiveTask(Task task);
    void completeTask(Task task);
}
