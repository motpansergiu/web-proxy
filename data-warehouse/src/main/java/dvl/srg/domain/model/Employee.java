package dvl.srg.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Employee implements Serializable {

    private UUID id;
    private String name;
    private String address;

    public static Employee of(final UUID id, final String name, final String address) {
        return new Employee(id, name, address);
    }
}

