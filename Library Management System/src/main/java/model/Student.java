package model;

public class Student {
    public int id;
    public String name;
    public String roll;

    public Student(int id, String name, String roll) {
        this.id = id;
        this.name = name;
        this.roll = roll;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (" + roll + ")";
    }
}
