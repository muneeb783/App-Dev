import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Project {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<Task> tasks;
    private List<ITeamMember> teamMembers;

    public Project(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        tasks = new ArrayList<>();
        teamMembers = new ArrayList<>();
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
        } else {
            throw new IllegalArgumentException("Cannot add null task");
        }
    }

    public void removeTask(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
        } else {
            throw new NoSuchElementException("Task is not in Project's task list");
        }
    }

    public void addTeamMember(ITeamMember teamMember) {
        if (teamMember != null) {
            teamMembers.add(teamMember);
        } else {
            throw new IllegalArgumentException("Cannot add null teamMember");
        }
    }

    public void removeTeamMember(ITeamMember teamMember) {
        if (teamMembers.contains(teamMember)) {
            teamMembers.remove(teamMember);
        } else {
            throw new NoSuchElementException("TeamMember is not in Project's TeamMember list");
        }
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public String getStartDate() {
        return this.startDate;
    }
    public String getEndDate() {
        return this.endDate;
    }
}