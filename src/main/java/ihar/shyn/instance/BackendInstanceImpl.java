package ihar.shyn.instance;

import java.util.Objects;

public class BackendInstanceImpl implements BackendInstance {
    private final String address;

    public BackendInstanceImpl(String address) {
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackendInstanceImpl that = (BackendInstanceImpl) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
