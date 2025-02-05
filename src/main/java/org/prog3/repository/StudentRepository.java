package org.prog3.repository;

import org.prog3.database.DataSource;
import org.prog3.mapper.SexMapper;
import org.prog3.model.Group;
import org.prog3.model.Sex;
import org.prog3.model.Student;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements  GenericDAO<Student>{
    private final DataSource dataSource;
    private final SexMapper sexMapper;

    public StudentRepository(DataSource dataSource, SexMapper sexMapper) {
        this.dataSource = dataSource;
        this.sexMapper = sexMapper;
    }

    @Override
    public List<Student> showAll() {
        List<Student> studentList = new ArrayList<>();
        String query = "SELECT * FROM student";
        try(PreparedStatement stm = dataSource.getConnection().prepareStatement(query)){
            ResultSet res = stm.executeQuery(query);

            while(res.next()){
                int groupId = res.getInt("groupId");
                Group group = new Group();
                group.setGroupId(groupId);

                Student students = new Student();
                students.setStudentId(res.getInt("studentId"));
                students.setStudentReference(res.getString("studentReference"));
                students.setLastName(res.getString("lastName"));
                students.setFirstName(res.getString("firstName"));
                students.setDateOfBirth(res.getObject("dateOfBirth", LocalDateTime.class));
                students.setSex(sexMapper.fromResultSetDbValue(res.getString("sex")));
                students.setGroup(group);
                studentList.add(students);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentList;
    }
    @Override
    public Student findById(int studentId) {
        Student studentFind = null;
        String query = "SELECT * FROM student WHERE studentId=?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement =connection.prepareStatement(query)){
            statement.setInt(1,studentId);
            ResultSet resultSet = statement.executeQuery();
            int groupId = resultSet.getInt("groupId");
            String groupName = resultSet.getString("groupName");
            Group group = new Group();
            group.setGroupId(groupId);
            group.setGroupName(groupName);

            if (resultSet.next()) {
                studentFind = new Student(
                        resultSet.getInt("studentId"),
                        resultSet.getString("studentReference"),
                        resultSet.getString("lastName"),
                        resultSet.getString("firstName"),
                        resultSet.getObject("dateOfBirth", LocalDateTime.class),
                        Sex.valueOf(resultSet.getString("sex")),
                        group
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentFind;
    }

    @Override
    public void delete(int studentId) {
        String query = "DELETE FROM student WHERE studentId=?";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1,studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createOrUpdate(Student newModel) {

    }

}

