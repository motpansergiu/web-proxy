package dvl.srg.repository;

import com.datastax.driver.core.Session;
import dvl.srg.domain.model.Employee;

import java.util.ArrayList;
import java.util.List;

public final class DefaultEmployeeRepository implements EmployeeRepository {

    private static final String TABLE_NAME = "employee";

    private final Session session;

    public DefaultEmployeeRepository(final Session session) {
        this.session = session;
    }

    @Override
    public void insertEmployee(final Employee employee) {
        session.execute(getInsertEmployeeStringQuery(new StringBuilder(), employee).toString());
    }

    @Override
    public void insertEmployeeBatch(final List<Employee> employees) {
        final StringBuilder sb = new StringBuilder("BEGIN BATCH ");
        employees.forEach(employee -> getInsertEmployeeStringQuery(sb, employee));
        sb.append("APPLY BATCH;");
        session.execute(sb.toString());
    }

    @Override
    public List<Employee> findAll() {
        final StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME);
        final List<Employee> employees = new ArrayList<>();
        session.execute(sb.toString())
                .forEach(row -> employees.add(Employee.of(row.getUUID("id"), row.getString("name"), row.getString("address"))));
        return employees;
    }

    private StringBuilder getInsertEmployeeStringQuery(final StringBuilder sb, final Employee employee) {
        return sb.append("INSERT INTO ")
                .append(TABLE_NAME)
                .append("(id, name, address) VALUES (")
                .append(employee.getId()).append(", '")
                .append(employee.getName()).append("', '")
                .append(employee.getAddress()).append("');");
    }
}
