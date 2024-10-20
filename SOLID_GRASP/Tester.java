public class Tester implements ITester {

    private String name;
    private String email;

    public Tester(String name, String email) {
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

    @Override
    public void leaveProject(Project project) {
        project.removeTeamMemeber(this);
    }

    @Override
    public void reportBug(String bugReport) {
        System.out.println(name + " reported bug: " + bugReport);
    }

    @Override
    public void receiveTestCase(String testCase) {
        System.out.println(name + " received test case: " + testCase);
    }
}
