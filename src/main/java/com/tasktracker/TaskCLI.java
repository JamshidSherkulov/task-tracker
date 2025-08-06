package com.tasktracker;

import java.io.IOException;
import java.util.Scanner;


public class TaskCLI {
    public static void main(String[] args) throws IOException {
        TaskManager manager = new TaskManager();

        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        switch (command.toLowerCase()) {
            case "add" -> {
                if (args.length < 2) {
                    System.out.println("Usage: add \"description\"");
                } else {
                    String description = combineArgs(args, 1);
                    manager.addTask(description);
                }
            }
            case "update" -> {
                if (args.length < 3) {
                    System.out.println("Usage: update <id> \"description\"");
                } else {
                    int id = Integer.parseInt(args[1]);
                    String newDescription = combineArgs(args, 2);
                    manager.updateTask(id, newDescription);
                }
            }
            case "delete" -> {
                if (args.length < 2) {
                    System.out.println("Usage: delete <id>");
                } else {
                    int id = Integer.parseInt(args[1]);
                    manager.deleteTask(id);
                }
            }
            case "mark-done" -> {
                if (args.length < 2) {
                    System.out.println("Usage: mark-done <id>");
                } else {
                    int id = Integer.parseInt(args[1]);
                    manager.markDone(id);
                }
            }
            case "mark-progress" -> {
                if (args.length < 2) {
                    System.out.println("Usage: mark-progress <id>");
                } else {
                    int id = Integer.parseInt(args[1]);
                    manager.markInProgress(id);
                }
            }
            case "list" -> {
                if (args.length == 1) {
                    manager.listAll();
                } else {
                    manager.listByStatus(args[1]);
                }
            }
            case "help" -> printHelp();
            default -> System.out.println("Unknown command. Type 'help' to see available commands.");
        }
    }

    // Combines multiple arguments into one description string
    private static String combineArgs(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private static void printHelp() {
        System.out.println("""
                Task Tracker CLI - Commands:
                ----------------------------------
                add "description"              → Add a new task
                update <id> "new description"  → Update task description
                delete <id>                    → Delete a task
                mark-done <id>                 → Mark task as done
                mark-progress <id>             → Mark task as in-progress
                list                           → List all tasks
                list <status>                  → List tasks by status (todo, in-progress, done)
                help                           → Show this help message
                """);
    }
}