package controlwoork;

import server.Generator;

public class Task {
    private String taskId;
    private String name;
    private String description;
    private Type taskType;

    public Task(String id, String name, String descr, Type taskType) {
        this.taskId = id;
        this.name = name;
        this.description = descr;
        this.taskType = taskType;
    }
    public Task() {
        this(Generator.makeId(),
                Generator.makeName(),
                Generator.makeDescription(),
                null);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getTaskType() {
        return taskType;
    }

    public void setTaskType(Type taskType) {
        this.taskType = taskType;
    }
}
