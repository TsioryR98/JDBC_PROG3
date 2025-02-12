import org.junit.jupiter.api.Test;
import org.prog3.model.Group;
import org.prog3.model.Order;
import org.prog3.model.Sex;
import org.prog3.model.Student;
import org.prog3.repository.Criteria;
import org.prog3.repository.OrderCriteria;
import org.prog3.repository.StudentRepository;

import java.security.AuthProvider;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.prog3.model.Sex.FEMALE;
import static org.prog3.model.Sex.MALE;

public class TestStudentRepository {
    StudentRepository subject = new StudentRepository();

    @Test
     void showAllStudent_pass(){
        List<Student> studentList = subject.showAll(1,10, Order.ASC);
        List<Student> maleList = subject.showAll(1,10, Order.ASC);
        List<Student> femaleList = subject.showAll(10,20, Order.ASC);

        assertEquals(10, studentList.size());
        assertTrue(maleList.stream()
                .allMatch(student -> student.getSex().equals(MALE)));
        assertTrue(femaleList.stream()
                .allMatch(student -> student.getSex().equals(FEMALE)));
    }

    @Test
    void readStudentByCriteria_pass() {
        List<Criteria> criteriaListName = new ArrayList<>();
        criteriaListName.add(new Criteria("last_name", "ran"));
        List<Student> studentListNameCriteria = subject.readStudentByCriteria(criteriaListName);
        assertEquals(3, studentListNameCriteria.size());

    }

    @Test
    void readStudentByCriteriaBoth_pass(){
        Date[] birthRange = {Date.valueOf("2000-01-01"), Date.valueOf("2001-12-31")};
        List<Criteria> criteriaListBoth= new ArrayList<>();
        criteriaListBoth.add(new Criteria("last_name", "ra"));
        criteriaListBoth.add(new Criteria("date_of_birth", birthRange));
        List<Student> studentListBothCriteria = subject.readStudentByCriteria(criteriaListBoth);
        assertEquals(11, studentListBothCriteria.size());
    }


    @Test
    void readOrderStudentByCriteria_pass() {
        List<OrderCriteria> orderCriteria = new ArrayList<>();
        orderCriteria.add(new OrderCriteria("last_name", Order.ASC));
        List<Student> students = subject.readOrderStudentByCriteria(orderCriteria);

        assertNotNull(students);
        assertEquals(23, students.size());
        assertEquals("Andria", students.get(0).getLastName());
        assertEquals(13, students.get(8).getStudentId());

        String actualName = subject.findById(students.get(8).getStudentId()).getFirstName();
        String expectedName = students.get(8).getFirstName();
        assertTrue(expectedName.equals(actualName));
    }

    @Test

    void PagedFilteredStudentByCriteria_test(){
        //criteria List that can handle a list of criteria ( birthRange, last name ILIKE)
        Date[] birthRange = {Date.valueOf("2000-01-01"), Date.valueOf("2001-12-31")};
        List<Criteria> criteriaListBoth= new ArrayList<>();
        criteriaListBoth.add(new Criteria("last_name", "ra"));
        criteriaListBoth.add(new Criteria("date_of_birth", birthRange));

        //List of column and order
        List<OrderCriteria> orderCriteria = new ArrayList<>();
        orderCriteria.add(new OrderCriteria("student_id", Order.DESC));

        // the method : public List<Student> PagedFilteredStudentByCriteria(List<Criteria> criteriaList, List<OrderCriteria> orderCriteriaList,int page, int size) {
        List<Student> PagedStudentListBothCriteria= subject.PagedFilteredStudentByCriteria(criteriaListBoth, orderCriteria, 1, 10);

        //test if not null
        assertNotNull(PagedStudentListBothCriteria);
        String actual = subject.findById(PagedStudentListBothCriteria.get(1).getStudentId()).getLastName();
        assertTrue(PagedStudentListBothCriteria.get(1).getLastName().equals(actual));



    }


    @Test
    void findById_pass(){
        //create from the test

        Student expectedStudent = new Student();
        int group_id = 3;
        Group group = new Group();
        group.setGroupId(group_id);
        expectedStudent.setStudentId(100);
        expectedStudent.setStudentReference("student_ref");
        expectedStudent.setLastName("student");
        expectedStudent.setFirstName("test");
        expectedStudent.setDateOfBirth(LocalDate.of(2000,1,1));
        expectedStudent.setSex(MALE);
        expectedStudent.setGroup(group);

        List<Student> findTestStudent = List.of(expectedStudent);
        subject.saveOrUpdate(findTestStudent);
        Student acStudent = subject.findById(expectedStudent.getStudentId());

        assertEquals(expectedStudent,acStudent);

        /*from database equals by lastname first name groupId */
        String actualTestValue = subject.findById(20).getLastName();
        assertEquals("Ravelo",actualTestValue,"test with getting names");
    }


    @Test
    void saveOrUpdate_pass() {
        Student studentTest1 = new Student();
            studentTest1.setStudentId(101);
            studentTest1.setStudentReference("STU03011");
            studentTest1.setFirstName("Randrian");
            studentTest1.setLastName("Feno");
            studentTest1.setDateOfBirth(LocalDate.of(2001, 5, 15));
            studentTest1.setSex(Sex.MALE);
            Group group1 = new Group();
            group1.setGroupId(3);
            studentTest1.setGroup(group1);

        Student studentTest2 = new Student();
            studentTest2.setStudentId(102);
            studentTest2.setStudentReference("STU31");
            studentTest2.setLastName("Rakotoson");
            studentTest2.setFirstName("Maya");
            studentTest2.setDateOfBirth(LocalDate.of(2002, 8, 22));
            studentTest2.setSex(Sex.FEMALE);
            Group group2 = new Group();
            group2.setGroupId(3);
            studentTest2.setGroup(group2);

        List<Student> studentToSave = List.of(studentTest1, studentTest2);
        subject.saveOrUpdate(studentToSave);
        assertEquals(2, studentToSave.size());


        Student studentTest3 = new Student();
        studentTest3.setStudentId(102);
        studentTest3.setStudentReference("STU311");
        studentTest3.setLastName("Rakotoson");
        studentTest3.setFirstName("May");
        studentTest3.setDateOfBirth(LocalDate.of(2002, 8, 22));
        studentTest3.setSex(Sex.FEMALE);
        Group group3 = new Group();
        group3.setGroupId(3);
        studentTest3.setGroup(group3);

        subject.saveOrUpdate(List.of(studentTest3));

        assertEquals("Randrian", studentToSave.get(0).getFirstName());
        assertEquals(Sex.MALE, studentToSave.get(0).getSex());
        assertEquals("Rakotoson", studentToSave.get(1).getLastName());
        assertEquals(Sex.FEMALE, studentToSave.get(1).getSex());

    }

    @Test
    void deleteById_pass(){
        Student expectedStudent = new Student();
        int group_id = 3;
        Group group = new Group();
        group.setGroupId(group_id);
        expectedStudent.setStudentId(90);
        expectedStudent.setStudentReference("student_ref_to_delete");
        expectedStudent.setLastName("student_to_delete");
        expectedStudent.setFirstName("test_to_delete");
        expectedStudent.setDateOfBirth(LocalDate.of(2000,1,1));
        expectedStudent.setSex(MALE);
        expectedStudent.setGroup(group);
        List<Student> findTestStudent = List.of(expectedStudent);

        subject.saveOrUpdate(findTestStudent);
        subject.delete(expectedStudent.getStudentId());
        assertNull(subject.findById(expectedStudent.getStudentId()));

    }


}