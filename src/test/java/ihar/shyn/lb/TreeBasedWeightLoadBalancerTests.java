package ihar.shyn.lb;

import ihar.shyn.model.BackendInstanceWithWeight;
import ihar.shyn.model.BackendInstanceWithWeightImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeBasedWeightLoadBalancerTests {

    @Test
    void whenRegisterSingleInstance_thenGetSingleInstance() {
        WeightLoadBalancer lb = new TreeBasedWeightLoadBalancer();
        BackendInstanceWithWeight instance = new BackendInstanceWithWeightImpl(20, "127.0.0.1");
        lb.register(instance);
        Assertions.assertEquals(lb.get(), instance);
    }

    @Test
    void whenRegisterNotValidInstanceInstance_thenReturnFalse() {
        WeightLoadBalancer lb = new TreeBasedWeightLoadBalancer();
        BackendInstanceWithWeight instance = new BackendInstanceWithWeightImpl(0, "127.0.0.1");

        Assertions.assertFalse(lb.register(null));
        Assertions.assertFalse(lb.register(instance));
    }

    @Test
    void whenRegisterSeveralInstances_thenGetSeveralInstances() {
        WeightLoadBalancer lb = new TreeBasedWeightLoadBalancer();
        BackendInstanceWithWeight instance1 = new BackendInstanceWithWeightImpl(50, "127.0.0.1");
        BackendInstanceWithWeight instance2 = new BackendInstanceWithWeightImpl(50, "127.0.0.2");
        lb.register(instance1);
        lb.register(instance2);

        boolean getFirst = false, getSecond = false;
        int maxAttemptCnt = 10000, currentAttempt = 0;

        while ((!getFirst || !getSecond) && currentAttempt < maxAttemptCnt) {
            currentAttempt++;
            if (lb.get().equals(instance1)) {
                getFirst = true;
            } else if (lb.get().equals(instance2)) {
                getSecond = true;
            }
        }

        Assertions.assertTrue(getFirst);
        Assertions.assertTrue(getSecond);
    }
}
