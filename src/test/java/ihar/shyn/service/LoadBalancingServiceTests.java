package ihar.shyn.service;

import ihar.shyn.model.BackendInstance;
import ihar.shyn.model.BackendInstanceImpl;
import ihar.shyn.model.LoadBalancerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoadBalancingServiceTests {

    @Test
    void whenCreateRoundRobinLoadBalancer_thenRoundRobinBehaviour() {
        LoadBalancingService loadBalancingService = new BasicLoadBalancingService(LoadBalancerType.ROUND_ROBIN, 10);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");
        loadBalancingService.register(instance1);
        loadBalancingService.register(instance2);
        loadBalancingService.register(instance3);

        Assertions.assertEquals(loadBalancingService.get(), instance1);
        Assertions.assertEquals(loadBalancingService.get(), instance2);
        Assertions.assertEquals(loadBalancingService.get(), instance3);
        Assertions.assertEquals(loadBalancingService.get(), instance1);
    }

    @Test
    void whenCreateRandomLoadBalancer_thenRandomBehaviour() {
        LoadBalancingService loadBalancingService = new BasicLoadBalancingService(LoadBalancerType.ROUND_ROBIN, 10);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");
        loadBalancingService.register(instance1);
        loadBalancingService.register(instance2);
        loadBalancingService.register(instance3);
        int maxAttemptsCount = 1000;
        int currentAttempt = 1;

        while(currentAttempt < maxAttemptsCount) {
            BackendInstance instance = loadBalancingService.get();
            String lastChar = instance.getAddress().substring(instance.getAddress().length()-1);
            if (Integer.parseInt(lastChar) != (currentAttempt % 3)) {
                break;
            }
            currentAttempt++;
        }
        System.out.println(currentAttempt);
        Assertions.assertNotEquals(currentAttempt, maxAttemptsCount);
    }
}
