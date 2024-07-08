package ihar.shyn.lb;

import ihar.shyn.model.BackendInstance;
import ihar.shyn.model.BackendInstanceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoundRobinLoadBalancerTests {
    @Test
    void whenCreateAndPutInstances_thenGetInstances() {
        LoadBalancer lb = new RoundRobinLoadBalancer(10);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");
        lb.register(instance1);
        lb.register(instance2);
        lb.register(instance3);
        Assertions.assertEquals(lb.get(), instance1);
        Assertions.assertEquals(lb.get(), instance2);
        Assertions.assertEquals(lb.get(), instance3);
        Assertions.assertEquals(lb.get(), instance1);
        Assertions.assertEquals(lb.get(), instance2);

    }

    @Test
    void whenRemoveInstance_thenGetCorrectInstance() {
        LoadBalancer lb = new RoundRobinLoadBalancer(10);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");
        lb.register(instance1);
        lb.register(instance2);
        lb.register(instance3);
        Assertions.assertEquals(lb.get(), instance1);
        Assertions.assertEquals(lb.get(), instance2);
        Assertions.assertEquals(lb.get(), instance3);
        Assertions.assertEquals(lb.get(), instance1);
        Assertions.assertEquals(lb.get(), instance2);
        lb.remove(instance3);
        Assertions.assertEquals(lb.get(), instance1);
        Assertions.assertEquals(lb.get(), instance2);
    }
}
