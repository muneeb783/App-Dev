public interface ITeamManager extends ITeamMember {

    void assignTask(Task task, TeamMember member);
    void updateProjectStatus(Project project, String status);
}
