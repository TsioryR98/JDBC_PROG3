package org.prog3.model;

import java.time.LocalDate;
import java.util.Objects;

public class Student {
    private int studentId;
    private String studentReference;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private Sex sex;
    private Group group;

    public Student(int studentId, String studentReference, String lastName, String firstName, LocalDate dateOfBirth, Sex sex, Group group) {
        this.studentId = studentId;
        this.studentReference = studentReference;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.group = group;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentReference() {
        return studentReference;
    }

    public void setStudentReference(String studentReference) {
        this.studentReference = studentReference;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Student() {
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getStudentId() == student.getStudentId() && Objects.equals(getStudentReference(), student.getStudentReference()) && Objects.equals(getLastName(), student.getLastName()) && Objects.equals(getFirstName(), student.getFirstName()) && Objects.equals(getDateOfBirth(), student.getDateOfBirth()) && getSex() == student.getSex() && Objects.equals(getGroup(), student.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentId(), getStudentReference(), getLastName(), getFirstName(), getDateOfBirth(), getSex(), getGroup());
    }
}



