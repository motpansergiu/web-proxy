package dvl.srg.domain.model;

import java.util.List;

public interface EmployeeRepository {

    void insertEmployee(final Employee employee);

    void insertEmployeeBatch(final List<Employee> employees);

    List<Employee> findAll();

}
