package bmt.Library;

public class Student {
    public String studentId;
    public String name;
    public String branch;

    public Student(String studentId, String name, String branch) {
        this.studentId = studentId;
        this.name = name;
        this.branch = branch;
    }

    public void display() {
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Branch: " + branch);
    }
}
