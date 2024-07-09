package ihar.shyn.service;

import ihar.shyn.lb.LoadBalancer;
import ihar.shyn.lb.RandomLoadBalancer;
import ihar.shyn.lb.RoundRobinLoadBalancer;
import ihar.shyn.model.BackendInstance;
import ihar.shyn.model.LoadBalancerType;

public class BasicLoadBalancingService implements LoadBalancingService {

    private final LoadBalancer loadBalancer;

    public BasicLoadBalancingService(LoadBalancerType loadBalancerType, int maxInstanceCount) {
        if (loadBalancerType.equals(LoadBalancerType.ROUND_ROBIN)) {
            loadBalancer = new RoundRobinLoadBalancer(maxInstanceCount);
        } else if (loadBalancerType.equals(LoadBalancerType.RANDOM)) {
            loadBalancer = new RandomLoadBalancer(maxInstanceCount);
        } else {
            throw new RuntimeException("Unknown load balancing type ....");
        }
    }

    @Override
    public boolean register(BackendInstance serverInstance) {
        return loadBalancer.register(serverInstance);
    }

    @Override
    public BackendInstance get() {
        return loadBalancer.get();
    }

    @Override
    public boolean remove(BackendInstance backendInstance) {
        return loadBalancer.remove(backendInstance);
    }
}
