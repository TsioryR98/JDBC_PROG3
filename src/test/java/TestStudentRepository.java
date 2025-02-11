import org.junit.jupiter.api.Test;
import org.prog3.model.Order;
import org.prog3.model.Sex;
import org.prog3.model.Student;
import org.prog3.repository.Criteria;
import org.prog3.repository.StudentRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestStudentRepository {
    StudentRepository subject = new StudentRepository();

    @Test
     void showAllStudent_pass(){
        List<Student> studentList = subject.showAll(1,10, Order.ASC);
        List<Student> maleList = subject.showAll(1,10, Order.ASC);
        List<Student> femaleList = subject.showAll(10,20, Order.ASC);

        assertEquals(10, studentList.size());
        assertTrue(maleList.stream()
                .allMatch(student -> student.getSex().equals(Sex.MALE)));
        assertTrue(femaleList.stream()
                .allMatch(student -> student.getSex().equals(Sex.FEMALE)));
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
}