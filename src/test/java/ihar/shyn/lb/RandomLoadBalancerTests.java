package ihar.shyn.lb;

import ihar.shyn.model.BackendInstance;
import ihar.shyn.model.BackendInstanceImpl;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class RandomLoadBalancerTests {

    @Test
    void whenRegisterInstanceBeforeLimit_thenReturnTrue_elseFalse() {
        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(3);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");
        BackendInstance instance4 = new BackendInstanceImpl("127.0.0.4");

        assertTrue(randomLoadBalancer.register(instance1));
        assertTrue(randomLoadBalancer.register(instance2));
        assertTrue(randomLoadBalancer.register(instance3));
        assertFalse(randomLoadBalancer.register(instance4));
    }

    @Test
    void whenRegisterNullAsInstance_thenExceptionThrows() {
        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(1);
        assertThrows(RuntimeException.class, () -> randomLoadBalancer.register(null));
    }

    @Test
    void whenGetServerFromNotEmptyLoadBalancer_thenGetInstance() {
        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(2);
        randomLoadBalancer.register(new BackendInstanceImpl("127.0.0.1"));
        assertNotNull(randomLoadBalancer.get());
    }

    @Test
    void whenGetServerFromEmptyLoadBalancer_thenGetNull() {
        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(2);
        assertNull(randomLoadBalancer.get());
    }

    @Test
    void whenRegisterSeveralInstancesAndGet_thenNeverGetNull() {
        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(5);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");
        BackendInstance instance4 = new BackendInstanceImpl("127.0.0.4");

        assertTrue(randomLoadBalancer.register(instance1));
        assertTrue(randomLoadBalancer.register(instance2));
        assertTrue(randomLoadBalancer.register(instance3));
        assertTrue(randomLoadBalancer.register(instance4));

        IntStream.range(1, 100).forEach((i) -> {
            BackendInstance serverInstance = randomLoadBalancer.get();
            assertNotNull(serverInstance);
        });
    }

    @Test
    // Not for production-mode test, just to ensure that random function is working properly
    void whenRegisterSeveralInstance_thenWaitRandomInstance() {
        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(5);
        BackendInstance instance1 = new BackendInstanceImpl("127.0.0.1");
        BackendInstance instance2 = new BackendInstanceImpl("127.0.0.2");
        BackendInstance instance3 = new BackendInstanceImpl("127.0.0.3");

        randomLoadBalancer.register(instance1);
        randomLoadBalancer.register(instance2);
        randomLoadBalancer.register(instance3);

        boolean firstReturned = false, secondReturned = false, thirdReturned = false;
        int attemptCount = 0;

        while ((!firstReturned || !secondReturned || !thirdReturned) && attemptCount <= 10000) {
            attemptCount++;
            BackendInstance serverInstance = randomLoadBalancer.get();
            if (serverInstance.equals(instance1)) {
                firstReturned = true;
            } else if (serverInstance.equals(instance2)) {
                secondReturned = true;
            } else if (serverInstance.equals(instance3)) {
                thirdReturned = true;
            }
        }

        assertTrue(firstReturned);
        assertTrue(secondReturned);
        assertTrue(thirdReturned);

    }

}
