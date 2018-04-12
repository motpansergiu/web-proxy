package dvl.srg.main;

import lombok.Value;

@Value
public class CassandraProperties {
    private String host;
    private int port;
    private String replicationStrategy;
    private int numberOfReplicas;
    private String keyspaceName;
}
