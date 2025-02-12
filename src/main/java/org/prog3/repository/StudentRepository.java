package org.prog3.repository;

import org.prog3.database.DataSource;
import org.prog3.mapper.SexMapper;
import org.prog3.model.Group;
import org.prog3.model.Order;
import org.prog3.model.Sex;
import org.prog3.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements  GenericDAO<Student> {
    private final DataSource dataSource;
    private final SexMapper sexMapper;

    //with args constructor with existing args
    public StudentRepository(DataSource dataSource, SexMapper sexMapper) {
        this.dataSource = dataSource;
        this.sexMapper = sexMapper;
    }

    public StudentRepository() {
        this.dataSource = new DataSource();
        this.sexMapper = new SexMapper();
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
    public List<Student> showAll(int page, int size, Order order) {
        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }
        String query = "SELECT * FROM student s ORDER BY s.sex " + order.name() + " LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            //put into the sql as parameter for resultset
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size * (page - 1));
            try (ResultSet res = preparedStatement.executeQuery()) {
                return mapStudentFromRes(res);
                //List<Student> studentList = mapStudentFromRes(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> saveOrUpdate(List<Student> models) {
        List<Student> newStudent = new ArrayList<>();
        String insertQuery = "INSERT INTO student (student_id, student_reference, last_name, first_name,date_of_birth, sex, group_id) VALUES (?,?,?,?,?,?::sex,?)";//force cast
        //count student_id
        String countExistStudent = "SELECT COUNT(*) FROM student WHERE student_id= ?";
        try (Connection connection = dataSource.getConnection()) {
            models.forEach(modelToSave -> {
                try (PreparedStatement countStatement = connection.prepareStatement(countExistStudent)) {
                    countStatement.setInt(1, modelToSave.getStudentId());
                    try (ResultSet resultSet = countStatement.executeQuery()) {
                        //one line for result in otherwise while
                        if (resultSet.next()) {
                            int countStudent = resultSet.getInt(1);
                            //mean student doest exist and > 0 exist
                            if (countStudent == 0) {
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                    insertStatement.setInt(1, modelToSave.getStudentId());
                                    insertStatement.setString(2, modelToSave.getStudentReference());
                                    insertStatement.setString(3, modelToSave.getLastName());
                                    insertStatement.setString(4, modelToSave.getFirstName());
                                    insertStatement.setDate(5, Date.valueOf(modelToSave.getDateOfBirth()));
                                    insertStatement.setString(6, modelToSave.getSex().name());
                                    insertStatement.setInt(7, modelToSave.getGroup().getGroupId());

                                    insertStatement.executeUpdate();
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // add student after insert
                newStudent.add(modelToSave);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newStudent;
    }

    @Override
    public Student findById(int id) {
        Student studentFind = null;
        String query = "SELECT * FROM student WHERE student_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int groupId = resultSet.getInt("group_id");
                    Group group = new Group();
                    group.setGroupId(groupId);
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentFind;
    }

    @Override
    public void delete(int student_id) {
        String query = "DELETE FROM student WHERE student_id=?";
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, student_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //filter by name and date of birth or both

    public List<Student> readStudentByCriteria(List<Criteria> criteriaList) {
        //WHERE 1=1 bypass name filter if it is null NOT pass directly into AND for birthdate
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE 1=1";
        List<String> conditions = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Criteria criteria : criteriaList) {
            String column = criteria.getColumn();
            Object value = criteria.getValue();
            if ("last_name".equals(column)) {
                conditions.add(" AND last_name ILIKE ?");
                values.add("%" + value + "%");
            } else if ("date_of_birth".equals(column)) {
                conditions.add(" AND date_of_birth BETWEEN ? AND ?");
                Date[] birthRange = (Date[]) value; //cast value ino DATE
                values.add(birthRange[0]);
                values.add(birthRange[1]);
            }
        }
        if (!conditions.isEmpty()) {
            //invert conditions to check
            sql += String.join(" ", conditions);
        }
        //put the value parameters inside conditions with set 1
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            int index = 1;
            //put into the sql as parameter for result

            for (Object value : values) {
                statement.setObject(index++, value);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    /*ORDER BY NAME or BIRTHDATE OR BOTH + SELECT * FROM student ORDER BY NAME ASC/DESC || ORDER BY BIRTHDATE ASC/DESC ||  SELECT * FROM student ORDER BY NAME ASC/DESC, BIRTHDATE ASC/DESC */

    public List<Student> readOrderStudentByCriteria(List<OrderCriteria> orderCriteriaList) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student";
        List<String> orderConditions = new ArrayList<>();

        for (OrderCriteria orderCriteria : orderCriteriaList) {
            orderConditions.add(orderCriteria.getColumn() + " " + orderCriteria.getOrder().name());
        }

        query += " ORDER BY " + String.join(", ", orderConditions);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

