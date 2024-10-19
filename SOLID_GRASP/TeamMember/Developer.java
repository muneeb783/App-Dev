public class Developer implements IDeveloper {

    private String name;
    private String email;

    public TeamManager(String name, String email) {
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

    public void leaveProject(Project project) {
        project.removeTeamMemeber(this);
    }

    @Override
    public void receiveTask(Task task) {
        System.out.println(name + " received task: " + task.getTitle());
    }

    @Override
    public void completeTask(Task task) {
        task.setStatus(TaskStatus.COMPLETED);
        System.out.println(name + " completed task: " + task.getTitle());
    }
    
}
