package bmt.Library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private StringProperty studentId;
    private StringProperty name;
    private StringProperty branch;

    public Student(String studentId, String name, String branch) {
        this.studentId = new SimpleStringProperty(studentId);
        this.name = new SimpleStringProperty(name);
        this.branch = new SimpleStringProperty(branch);
    }

    // Property getters for JavaFX
    public StringProperty studentIdProperty() {
        return studentId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty branchProperty() {
        return branch;
    }

    // Simple getters
    public String getStudentId() {
        return studentId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getBranch() {
        return branch.get();
    }

    public void display() {
        System.out.println("Student ID: " + getStudentId());
        System.out.println("Name: " + getName());
        System.out.println("Branch: " + getBranch());
    }
}
