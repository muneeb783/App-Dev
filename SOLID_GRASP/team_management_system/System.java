package solid_grasp.team_management_system;
import java.util.ArrayList;
import java.util.List;

public class System {
    private static System instance;

    private List<Project> projects;
    
    private System() {
        projects = new ArrayList<>();
    }

    public static System getInstance() {
        if (instance == null) {
            instance = new System();
        }
        return instance;
    }

    public void addProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Cannot add null project to System");
        }
        projects.add(project);
    }

    public void removeProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Cannot remove null project to System");
        }
        projects.remove(project);
    }

}

