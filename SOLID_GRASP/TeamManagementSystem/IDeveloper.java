package SOLID_GRASP.TeamManagementSystem;
public interface IDeveloper extends ITeamMember {



    void receiveTask(Task task);
    void completeTask(Task task);
}
