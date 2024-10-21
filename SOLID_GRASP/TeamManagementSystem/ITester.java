package SOLID_GRASP.TeamManagementSystem;
public interface ITester extends ITeamMember {
    
    void receiveTestCase(String testCase);
    void reportBug(String bugReport);
}