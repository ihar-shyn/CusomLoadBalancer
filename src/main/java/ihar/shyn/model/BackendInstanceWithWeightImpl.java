package ihar.shyn.model;

import java.util.Objects;

public class BackendInstanceWithWeightImpl implements BackendInstanceWithWeight {

    private final int weight;
    private final String address;

    public BackendInstanceWithWeightImpl(int weight, String address) {
        this.weight = weight;
        this.address = address;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackendInstanceWithWeightImpl that = (BackendInstanceWithWeightImpl) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
