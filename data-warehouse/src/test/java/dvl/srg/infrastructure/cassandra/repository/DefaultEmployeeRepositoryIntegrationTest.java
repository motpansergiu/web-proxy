package dvl.srg.infrastructure.cassandra.repository;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import dvl.srg.infrastructure.cassandra.connector.CassandraConnector;
import dvl.srg.infrastructure.cassandra.keyspace.KeyspaceManager;
import dvl.srg.domain.model.Employee;
import dvl.srg.domain.model.EmployeeRepository;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultEmployeeRepositoryIntegrationTest {

    private static final String KEYSPACE_NAME = "testEmployessKeySpace";

    private static final String EMPLOYEE = "employee";

    private KeyspaceManager schemaRepository;

    private EmployeeRepository employeeRepository;

    private DDLEmployeeCassandra ddlEmployeeRepository;

    private Session session;


    @BeforeClass
    public static void init() throws ConfigurationException, TTransportException, IOException, InterruptedException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
    }

    @Before
    public void connect() {
        final CassandraConnector client = new CassandraConnector();
        client.connect("127.0.0.1", 9142);
        this.session = client.getSession();
        schemaRepository = new KeyspaceManager(session);
        schemaRepository.createKeyspace(KEYSPACE_NAME, "SimpleStrategy", 1);
        schemaRepository.useKeyspace(KEYSPACE_NAME);
        employeeRepository = new DefaultEmployeeRepository(session);
        ddlEmployeeRepository = new DDLEmployeeCassandra();
    }

    @Test
    public void whenCreatingATableThenCreatedCorrectly() {
        ddlEmployeeRepository.deleteTable(session);
        ddlEmployeeRepository.createTable(session);

        final ResultSet result = session.execute("SELECT * FROM " + KEYSPACE_NAME + "." + EMPLOYEE + ";");

        // Collect all the column names in one list.
        final List<String> columnNames = result.getColumnDefinitions()
                .asList()
                .stream()
                .map(ColumnDefinitions.Definition::getName)
                .collect(toList());

        assertEquals(columnNames.size(), 3);
        assertTrue(columnNames.contains("id"));
        assertTrue(columnNames.contains("name"));
        assertTrue(columnNames.contains("address"));
    }

    @Test
    public void whenAddingANewEmployeeThenEmployeeExists() {
        ddlEmployeeRepository.deleteTable(session);
        ddlEmployeeRepository.createTable(session);

        final String name = "Kapushon";
        final String address = "Leuseni";
        Employee employee = Employee.of(UUIDs.timeBased(), name, address);
        employeeRepository.insertEmployee(employee);

        List<Employee> savedEmployee = employeeRepository.findAll();
        assertEquals(savedEmployee.get(0).getName(), employee.getName());
        assertEquals(savedEmployee.get(0).getAddress(), employee.getAddress());
    }

    @Test
    public void whenAddingANewEmployeeBatchThenEmployeesExist() {
        ddlEmployeeRepository.deleteTable(session);
        ddlEmployeeRepository.createTable(session);

        final String name = "Kapushon";
        final String address = "Leuseni";
        final List<Employee> employees = new ArrayList<>(3);
        employees.add(Employee.of(UUIDs.timeBased(), name, address));
        employees.add(Employee.of(UUIDs.timeBased(), name, address));
        employees.add(Employee.of(UUIDs.timeBased(), name, address));

        employeeRepository.insertEmployeeBatch(employees);

        List<Employee> savedEmployee = employeeRepository.findAll();

        assertEquals(3, savedEmployee.size());
        assertTrue(savedEmployee.stream().anyMatch(e -> name.equals(e.getName()) && address.equals(e.getAddress())));
    }


    @AfterClass
    public static void cleanup() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}