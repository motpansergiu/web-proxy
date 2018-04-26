package dvl.srg.main;

import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyFileLoaderTest {

    @Test
    public void whenLoadPropetiesFileThenCassandraPropertiesIsCreated() throws IOException {
        final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final String appConfigPath = rootPath + "cassandra.properties";

        CassandraProperties cassandraProperties = PropertyFileLoader.loadCassandraProperties(appConfigPath);
        assertNotNull(cassandraProperties);
        assertEquals("127.0.0.1",cassandraProperties.getHost());
        assertEquals(9042, cassandraProperties.getPort());
        assertEquals("SimpleStrategy",cassandraProperties.getReplicationStrategy());
        assertEquals("employeeKeySpace",cassandraProperties.getKeyspaceName());
        assertEquals(1, cassandraProperties.getNumberOfReplicas());

    }

    @Test
    public void whenLoadPropetiesFileThenApplicationPropertiesIsCreated() throws IOException {
        final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final String appConfigPath = rootPath + "application.properties";

        ApplicationProperties applicationProperties = PropertyFileLoader.loadApplicationProperties(appConfigPath);

        assertNotNull(applicationProperties);
        assertEquals(9991, applicationProperties.getServerPort());
    }

    @Test(expected = NullPointerException.class)
    public void whenLoadPropetiesFileAndFilePathNullThenThrowsNullPointerException() throws IOException {
        CassandraProperties cassandraProperties = PropertyFileLoader.loadCassandraProperties(null);
    }

    @Test(expected = IOException.class)
    public void whenLoadPropetiesFileAndFileNotFoundThenThrowsIOException() throws IOException {
        CassandraProperties cassandraProperties = PropertyFileLoader.loadCassandraProperties("");
    }
}