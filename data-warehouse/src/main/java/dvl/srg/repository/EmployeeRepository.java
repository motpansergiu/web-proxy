package dvl.srg.repository;

import dvl.srg.domain.model.Employee;

import java.util.List;

public interface EmployeeRepository {

    void insertEmployee(final Employee employee);

    void insertEmployeeBatch(final List<Employee> employees);

    List<Employee> findAll();

}
