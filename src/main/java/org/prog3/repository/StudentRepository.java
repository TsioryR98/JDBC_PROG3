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

public class StudentRepository implements  GenericDAO<Student> {
    private final DataSource dataSource;
    private final SexMapper sexMapper;

    public StudentRepository(DataSource dataSource, SexMapper sexMapper) {
        this.dataSource = dataSource;
        this.sexMapper = sexMapper;
    }

    private List<Student> mapStudentFromRes(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            int groupId = resultSet.getInt("group_id");
            Group group = new Group();
            group.setGroupId(groupId);

            Student student = new Student();
            student.setStudentId(resultSet.getInt("student_id"));
            student.setStudentReference(resultSet.getString("student_reference"));
            student.setLastName(resultSet.getString("last_name"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setDateOfBirth(resultSet.getDate("date_of_birth").toLocalDate());
            student.setSex(sexMapper.fromResultSetDbValue(resultSet.getString("sex")));
            student.setGroup(group);
            students.add(student);

        }
        return students;
    }

    ;

    @Override
    public List<Student> showAll(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }
        String query = "SELECT * FROM student";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size * (page - 1));
            try (ResultSet res = preparedStatement.executeQuery(query)) {
                List<Student> studentList = mapStudentFromRes(res);
                return studentList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> saveOrUpdate(List<Student> models) {
        List<Student> newStudent = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            models.forEach(modelToSave -> {
                String query = "INSERT INTO student (student_id, student_reference, last_name, first_name,date_of_birth, sex, group_id) VALUE (?,?,?,?,?,?,?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, modelToSave.getStudentId());
                    preparedStatement.setString(2, modelToSave.getStudentReference());
                    preparedStatement.setString(3, modelToSave.getLastName());
                    preparedStatement.setString(4, modelToSave.getFirstName());
                    preparedStatement.setDate(5, Date.valueOf(modelToSave.getDateOfBirth()));//if birthday is lcoalDate
                    //preparedStatement.setObject(5, modelToSave.getDateOfBirth());//lcoaldatetime
                    preparedStatement.setString(6, modelToSave.getSex().name());
                    preparedStatement.setInt(7, modelToSave.getGroup().getGroupId());

                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newStudent.add(findById(modelToSave.getStudentId()));
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return newStudent;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Student findById(int id) {
        Student studentFind = null;
        String query = "SELECT * FROM student WHERE student_id=?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement =connection.prepareStatement(query)){
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            int groupId = resultSet.getInt("group_id");
            String groupName = resultSet.getString("group_name");
            Group group = new Group();
            group.setGroupId(groupId);
            group.setGroupName(groupName);

            if (resultSet.next()) {
                studentFind = new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("student_reference"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        Sex.valueOf(resultSet.getString("sex")),
                        group
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentFind;
    }


}

