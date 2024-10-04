import java.util.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        InputScanner inputScanner = new InputScanner();

        System.out.println("╭─━─━─━─━─━─━─━─━─≪✠≫━─━─━─━─━─━─━─━─━╮\n" +
                " 📋 Welcome to Task Manager! 📋");

        while (true) {
            int action = inputScanner.askAction();
            switch (action) {
                case InputScanner.ERROR:
                    return;
                case InputScanner.EXIT:
                    System.out.println("Good Bye!");
                    return;
                case InputScanner.VIEW:
                    List<Task> tasks = taskManager.getAllTasks();
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks available. Maybe you'd like to add some tasks?");
                    } else {
                        PrintManager.printList(tasks);
                    }
                    break;
                case InputScanner.ADD:
                    Task task = inputScanner.enterTask();
                    taskManager.addTask(task);
                    System.out.println("Task is added!");
                    break;
                case InputScanner.EDIT:
                    PrintManager.printList(taskManager.getAllTasks());
                    int id = inputScanner.askId();

                    if (id > 0) {
                        task = inputScanner.enterTask();
                        taskManager.editTask(id, task);
                        System.out.println("Task is changed!");
                    }
                    break;
                case InputScanner.DELETE:
                    PrintManager.printList(taskManager.getAllTasks());
                    id = inputScanner.askId();

                    if (id > 0) {
                        taskManager.deleteTask(id);
                        System.out.println("Task deleted!");
                    }
                    break;
                default:
                    System.out.println("Please enter the correct number of action!");
            }
        }
    }
}

class Task {
    private int id;
    private String title;
    private String description;

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "id: " + id + " | title: " + title + " | description: " + description;
    }
}

class HighPriorityTask extends Task {
    public HighPriorityTask(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public String toString() {
        return "[HIGH] " + super.toString();
    }
}

class MediumPriorityTask extends Task {
    public MediumPriorityTask(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public String toString() {
        return "[MEDIUM] " + super.toString();
    }
}

class LowPriorityTask extends Task {
    public LowPriorityTask(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public String toString() {
        return "[LOW] " + super.toString();
    }
}

class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private int taskIdCounter = 1;

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        task.setId(taskIdCounter++);
        tasks.add(task);
        tasks.sort(Comparator.comparingInt(task1 -> {
            if (task1 instanceof HighPriorityTask) return 1;
            if (task1 instanceof MediumPriorityTask) return 2;
            return 3;
        }));
    }

    public void editTask(int id, Task task) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                if (!task.getTitle().isEmpty()) {
                    t.setTitle(task.getTitle());
                }
                if (!task.getDescription().isEmpty()) {
                    t.setDescription(task.getDescription());
                }
                return;
            }
        }
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }
}

class InputScanner {
    public static final int ERROR = -1;
    public static final int EXIT = 0;
    public static final int VIEW = 1;
    public static final int ADD = 2;
    public static final int EDIT = 3;
    public static final int DELETE = 4;

    private Scanner scanner = new Scanner(System.in);

    public int askAction() {
        System.out.println("━─━───────────────༺༻───────────────━─━" +
                "\n| [1]: View Tasks                     |");
        System.out.println("| [2]: Add Task                       |");
        System.out.println("| [3]: Edit Task                      |");
        System.out.println("| [4]: Delete Task                    |");
        System.out.println("| [0]: Exit application               |\n" +
                "╰─━─━─━─━─━─━─━─━─≪✠≫─━─━─━─━─━─━─━─━─╯\n" +
                "➥ Choose an action: ");

        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Please enter the number of action!");
            return askAction();
        } catch (Exception e) {
            System.out.println("Unknown error: " + e.getMessage());
            return ERROR;
        }
    }

    public Task enterTask() {
        System.out.println("Enter Task Title (or press Enter to skip):");
        String title = scanner.nextLine();

        System.out.println("Enter Task Description (or press Enter to skip):");
        String description = scanner.nextLine();

        System.out.println("Enter Task Priority (1: High, 2: Medium, 3: Low):");
        int priority = Integer.parseInt(scanner.nextLine());

        switch (priority) {
            case 1:
                return new HighPriorityTask(-1, title, description);
            case 2:
                return new MediumPriorityTask(-1, title, description);
            case 3:
                return new LowPriorityTask(-1, title, description);
            default:
                return new LowPriorityTask(-1, title, description);
        }
    }

    public int askId() {
        System.out.println("Enter id of the task:");
        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Please enter the number!");
            return askId();
        } catch (Exception e) {
            System.out.println("Unknown error: " + e.getMessage());
            return ERROR;
        }
    }
}

class PrintManager {
    static void printList(List<Task> list) {
        for (Task task : list) {
            System.out.println(task.toString());
        }
    }
}
