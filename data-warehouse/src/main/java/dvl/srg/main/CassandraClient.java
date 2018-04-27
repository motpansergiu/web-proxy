package dvl.srg.main;

import dvl.srg.configuration.CassandraConnector;
import dvl.srg.domain.model.Employee;
import dvl.srg.repository.DDLEmployeeRepository;
import dvl.srg.repository.DefaultEmployeeRepository;
import dvl.srg.repository.EmployeeRepository;
import dvl.srg.configuration.KeyspaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

import java.util.ArrayList;
import java.util.List;

public class CassandraClient {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraClient.class);

    public static void main(String args[]) {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceManager sr = new KeyspaceManager(session);
        sr.createKeyspace("employeeKeySpace", "SimpleStrategy", 1);
        sr.useKeyspace("employeeKeySpace");

//        DDLEmployeeRepository ddlEmployeeRepository = new DDLEmployeeRepository(session);
        EmployeeRepository employeeRepository = new DefaultEmployeeRepository(session);

        //ddlEmployeeRepository.createTable();

//        employeeRepository.insertEmployee(Employee.of(UUIDs.timeBased(), "Sergiu Motpan", "Chisinau"));

//        employeeRepository.insertEmployeeBatch(getDefaultEmployeesList());

        employeeRepository.findAll().forEach(employee -> System.out.println("Employee in list: " + employee));

       // ddlEmployeeRepository.deleteTable();

       // sr.deleteKeyspace("employeeKeySpace");

        connector.close();
    }

    private static List<Employee> getDefaultEmployeesList() {
        final List<Employee> employees = new ArrayList<>(5);
        employees.add(Employee.of(UUIDs.timeBased(), "John Doe", "London"));
        employees.add(Employee.of(UUIDs.timeBased(), "Steve Jobs", "Washington"));
        employees.add(Employee.of(UUIDs.timeBased(), "Bill GAtes", "Seattle"));
        employees.add(Employee.of(UUIDs.timeBased(), "Denis Ritch", "Berlin"));
        employees.add(Employee.of(UUIDs.timeBased(), "Elon Musk", "London"));
        return employees;
    }
}
