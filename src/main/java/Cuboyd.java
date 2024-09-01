import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Cuboyd {

    public static HashMap<String, String> parseCommandToArguments(String line) {
        HashMap<String, String> argumentsList = new HashMap<>();
        String[] lineArgs = line.split(" ");

        // Command
        if (lineArgs.length <= 0) {
            argumentsList.put("command","");
            return argumentsList;
        }
        argumentsList.put("command",lineArgs[0]);

        // Arguments
        String currArgumentName = "main";
        StringBuilder currArgument = new StringBuilder();

        for (int i=1; i<lineArgs.length; i++) {
            if (lineArgs[i].isEmpty()) { // Should be redundant but just in case
                continue;
            }
            if (lineArgs[i].charAt(0) == '/') {
                // New argument
                if (!currArgument.toString().isEmpty()){
                    argumentsList.put(currArgumentName, currArgument.toString().strip());
                }
                currArgumentName = lineArgs[i];
                currArgument.setLength(0);
            } else {
                // Add on to existing argument
                currArgument.append(" ").append(lineArgs[i]);
            }
        }
        // Add last command
        if (!currArgument.toString().isEmpty()) {
            argumentsList.put(currArgumentName, currArgument.toString().strip());
        }
        return argumentsList;
    }

    public static void main(String[] args) {
        String name = "Cuboyd";
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");

        // Initialise List
        ArrayList<Task> tasks = new ArrayList<>();

        // For Marking/ Unmarking
        Task currentTask;
        int index;

        // Command Entry
        String line;
        HashMap<String, String> argumentsList;
        Scanner scanner = new Scanner(System.in);
        boolean isAskingInput = true;
        while (isAskingInput){
            System.out.print("> ");
            line = scanner.nextLine();
            argumentsList = parseCommandToArguments(line);
            switch(argumentsList.get("command")){
                case "list":
                    for (int currentItemIndex = 0; currentItemIndex< tasks.size(); currentItemIndex++){
                        System.out.println(String.valueOf(currentItemIndex+1) + "." + tasks.get(currentItemIndex));
                    }
                    break;
                case "mark":
                    if (argumentsList.get("main") == null){
                        System.out.println("No index was given!");
                        break;
                    }
                    try {
                        index = Integer.parseInt(argumentsList.get("main")) - 1;
                    } catch (NumberFormatException e){
                        System.out.println("That is not a valid index!");
                        break;
                    }
                    if (index < 0 || index >= tasks.size()){
                        System.out.println("That is not a valid index!");
                        break;
                    }
                    currentTask = tasks.get(index);
                    currentTask.markAsDone();

                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + currentTask);
                    break;
                case "unmark":
                    if (argumentsList.get("main") == null){
                        System.out.println("No index was given!");
                        break;
                    }
                    try {
                        index = Integer.parseInt(argumentsList.get("main")) - 1;
                    } catch (NumberFormatException e){
                        System.out.println("That is not a valid index!");
                        break;
                    }
                    if (index < 0 || index >= tasks.size()){
                        System.out.println("That is not a valid index!");
                        break;
                    }
                    currentTask = tasks.get(index);
                    currentTask.markAsUndone();

                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + currentTask);
                    break;
                case "bye":
                    System.out.println("Bye. Hope to see you again soon!");
                    isAskingInput = false;
                    break;
                default:
                    tasks.add(new Task(line));
                    System.out.println("added: " + line);
                    break;
            }
        }
    }
}