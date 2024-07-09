package ihar.shyn.instance;

import ihar.shyn.model.BackendInstanceWithWeight;
import ihar.shyn.model.BackendInstanceWithWeightImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BackendInstanceWithWeightImplTests {

    @Test
    void whenCreateWithAddressAndWeight_thenGetSameAddressAndWeight() {
        String address = "127.0.0.1";
        int weight = 20;

        BackendInstanceWithWeight instance = new BackendInstanceWithWeightImpl(weight, address);

        Assertions.assertEquals(instance.getAddress(), address);
        Assertions.assertEquals(instance.getWeight(), weight);
    }

    @Test
    void whenCreateSeveralInstance_thenEqualsWorksCorrectly() {
        String address1 = "127.0.0.1";
        String address2 = "127.0.0.2";
        int weight1 = 20;

        BackendInstanceWithWeight instance1 = new BackendInstanceWithWeightImpl(weight1, address1);
        BackendInstanceWithWeight instance2 = new BackendInstanceWithWeightImpl(weight1, address1);
        BackendInstanceWithWeight instance3 = new BackendInstanceWithWeightImpl(weight1, address2);

        Assertions.assertEquals(instance1, instance2);
        Assertions.assertNotEquals(instance1, instance3);
    }
}
