import java.util.HashMap;
import java.util.Scanner;

public class Ui {
    public TaskList taskList;
    public Storage storage;

    /**
     * Creates the UI Object and loads the save data
     */
    public Ui(){
        taskList = new TaskList();
        storage = new Storage("./savedata.txt");

        try{
            storage.load(taskList);
        } catch (CuboydException e) {
            System.out.println(e.getMessage());
        }
    }

    // Helpers /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Displays task that was added and new task list size.
     * @param task Task to Add
     */
    public void addTaskWithUI(Task task) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task.toString());
        System.out.printf("Now you have %d tasks in the list.\n", taskList.size());
    }

    // Intro Text //////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Displays an Introductory message to the user
     */
    public static void displayIntroText(){
        String name = "Cuboyd";
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    // Command Parsing /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Matches the argument list to a related command and runs said command
     * @param argumentsList List of arguments
     * @return Whether to continue running the program
     * @throws CuboydException If command fails to run
     */
    public boolean commandMatching(HashMap<String, String> argumentsList) throws CuboydException {
        Task currentTask;
        switch(argumentsList.get(Parser.ARGUMENT_COMMAND)){
        case "list":
            System.out.println("Here are the tasks in your list:");
            System.out.print(taskList.listAllTasks());
            break;
        case "find":
            System.out.println("Here are the matching tasks in your list:");
            System.out.print(taskList.listFoundTasks(argumentsList.get(Parser.ARGUMENT_MAIN)));
            break;
        case "todo":
            addTaskWithUI(taskList.addTodo(argumentsList.get(Parser.ARGUMENT_MAIN)));
            storage.save(taskList);
            break;
        case "deadline":
            addTaskWithUI(taskList.addDeadline(argumentsList.get(Parser.ARGUMENT_MAIN), argumentsList.get("/by")));
            storage.save(taskList);
            break;
        case "event":
            addTaskWithUI(taskList.addEvent(argumentsList.get(Parser.ARGUMENT_MAIN),
                    argumentsList.get("/from"), argumentsList.get("/to")));
            storage.save(taskList);
            break;
        case "mark":
            currentTask = taskList.markTask(argumentsList.get(Parser.ARGUMENT_MAIN));
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + currentTask.toString());
            storage.save(taskList);
            break;
        case "unmark":
            currentTask = taskList.unmarkTask(argumentsList.get(Parser.ARGUMENT_MAIN));
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + currentTask.toString());
            storage.save(taskList);
            break;
        case "delete":
            currentTask = taskList.deleteTask(argumentsList.get(Parser.ARGUMENT_MAIN));
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + currentTask.toString());
            System.out.printf("Now you have %d tasks in the list.\n", this.taskList.size());
            storage.save(taskList);
            break;
        case "bye":
            System.out.println("Bye. Hope to see you again soon!");
            return false;
        default:
            System.out.println("No valid command given! Valid Commands are: \n" +
                    "  - list\n" +
                    "  - todo\n" +
                    "  - deadline\n" +
                    "  - event\n" +
                    "  - mark\n" +
                    "  - unmark\n" +
                    "  - delete\n" +
                    "  - load\n" +
                    "  - save\n" +
                    "  - bye"
            );
            break;
        }
        return true;
    }

    /**
     * Runs a loop where the user can enter commands, until they exit
     */
    public void commandEntryLoop(){
        // Command Entry
        String line;
        HashMap<String, String> argumentsList;
        Scanner scanner = new Scanner(System.in);
        boolean isAskingInput = true;
        while (isAskingInput){
            System.out.print("> ");
            line = scanner.nextLine();
            argumentsList = Parser.parseCommandToArguments(line);
            try {
                isAskingInput = commandMatching(argumentsList);
            } catch (CuboydException e){
                System.out.println(e.getMessage());
            }
        }
    }

    // Run /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Runs the entire program
     */
    public void run(){
        displayIntroText();
        commandEntryLoop();
    }
}