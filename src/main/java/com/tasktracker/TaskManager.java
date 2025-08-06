package com.tasktracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final String FILE_NAME = "tasks.json";
    private List<Task> tasks;
    private Gson gson;

    public TaskManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        this.tasks = loadTasks();
    }

    private List<Task> loadTasks() {
        try {
            if (!Files.exists(Paths.get(FILE_NAME))) {
                return new ArrayList<>();
            }

            Reader reader = Files.newBufferedReader(Paths.get(FILE_NAME));
            Type taskListType = new TypeToken<List<Task>>() {}.getType();
            return gson.fromJson(reader, taskListType);
        } catch (IOException e) {
            System.out.println("Error reading task file");
            return new ArrayList<>();
        }
    }

    private void saveTasks() throws IOException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            gson.toJson(tasks, writer);
        } catch (IOException ex) {
            System.out.println("Error saving tasks.");
        }
    }

    private int generateId() {
        return tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }

    public void addTask(String description) throws IOException {
        int id = generateId();
        Task task = new Task(id, description);
        tasks.add(task);
        saveTasks();
        System.out.println("Task added successfully (ID: " + id + ")");
    }

    public void updateTask(int id, String newDescription) throws IOException {
        Task task = findTaskById(id);
        if (task != null) {
            task.setDescription(newDescription);
            saveTasks();
            System.out.println("Task updated successfully");
        } else {
            System.out.println("Task not found!");
        }
    }

    public void deleteTask(int id) throws IOException {
        Task task = findTaskById(id);
        if (task != null) {
            tasks.remove(task);
            saveTasks();
            System.out.println("Task deleted successfully");
        } else {
            System.out.println("Task not found!");
        }
    }

    public void markInProgress(int id) throws IOException {
        changeStatus(id, "in-progress");
    }

    public void markDone(int id) throws IOException {
        changeStatus(id, "done");
    }

    private void changeStatus(int id, String status) throws IOException {
        Task task = findTaskById(id);
        if (task != null){
            task.setStatus(status);
            saveTasks();
            System.out.println("Task marked as " + status + ".");
        } else {
            System.out.println("Task not found!");
        }
    }

    public void listAll() {
        if (tasks.isEmpty()) {
            System.out.println("No task found!");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    public void listByStatus(String status) {
        List<Task> filtered = tasks.stream()
                            .filter(t -> t.getStatus().equalsIgnoreCase(status))
                            .toList();

        if (filtered.isEmpty())  {
            System.out.println("No task with status " + status);
        } else {
            filtered.forEach(System.out::println);
        }


    }

    private Task findTaskById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

}
