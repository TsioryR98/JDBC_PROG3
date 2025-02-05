package org.prog3.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Group {
    private int groupId;
    private String groupName;
    private LocalDateTime groupYear;
    private int studentNb;
    private Student students;

    public Group(int groupId, String groupName, LocalDateTime groupYear, int studentNb, Student students) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupYear = groupYear;
        this.studentNb = studentNb;
        this.students = students;
    }

    public Group() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getGroupYear() {
        return groupYear;
    }

    public void setGroupYear(LocalDateTime groupYear) {
        this.groupYear = groupYear;
    }

    public int getStudentNb() {
        return studentNb;
    }

    public void setStudentNb(int studentNb) {
        this.studentNb = studentNb;
    }

    public Student getStudents() {
        return students;
    }

    public void setStudents(Student students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return getGroupId() == group.getGroupId() && getStudentNb() == group.getStudentNb() && Objects.equals(getGroupName(), group.getGroupName()) && Objects.equals(getGroupYear(), group.getGroupYear()) && Objects.equals(getStudents(), group.getStudents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getGroupName(), getGroupYear(), getStudentNb(), getStudents());
    }
}

