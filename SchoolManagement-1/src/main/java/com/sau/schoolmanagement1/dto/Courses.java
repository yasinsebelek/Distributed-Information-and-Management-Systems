package com.sau.schoolmanagement1.dto;

public class Courses {
    private int id;
    private String title;
    private  String description;
    private String semester;

    public  Courses() {};

    public Courses(int id, String title, String description, String semester) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.semester = semester;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", semester='" + semester + '\'' +
                '}';
    }

//    @Override
//    public String toString() {
//        return String.format(
//                "Courses{id=%d, title='%s', description='%s', semester='%s'}",
//                id, title, description, semester
//        );
//    }
}
