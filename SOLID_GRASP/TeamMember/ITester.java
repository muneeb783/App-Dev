public interface Tester extends TeamMember {
    
    void receiveTestCase(String testCase);
    void reportBug(String bugReport);
    void verifyTaskCompletion(Task task);
}