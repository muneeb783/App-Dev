public class TeamMember implements ITeamMember {
    
    private String name;
    private String email;

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public void joinProject(Project project) {
        project.addTeamMember(this);
    }

    public void leaveProject(Project project) {
        project.removeTeamMemeber(this);
    }
}
