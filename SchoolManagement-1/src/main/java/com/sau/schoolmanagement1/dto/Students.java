package com.sau.schoolmanagement1.dto;

public class Students {
    private int id;
    private String name;
    private String department;

    public Students() {}


    public Students(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Students{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }


    //    @Override
//    public String toString() {
//        return String.format("Student{id : %d, name : '%s', department : '%s'}", id, name, department);
//    }
}
