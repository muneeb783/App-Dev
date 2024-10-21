package solid_grasp.team_management_system;
public interface ITester extends ITeamMember {
    
    void receiveTestCase(String testCase);
    void reportBug(String bugReport);
}