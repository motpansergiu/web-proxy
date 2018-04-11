package dvl.srg.domain.model;

import java.io.Serializable;
import java.util.UUID;

public final class Employee implements Serializable {

    private UUID id;
    private String name;
    private String address;

    private Employee(final UUID id, final String name, final String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static Employee of(final UUID id, final String name, final String address) {
        return new Employee(id, name, address);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

