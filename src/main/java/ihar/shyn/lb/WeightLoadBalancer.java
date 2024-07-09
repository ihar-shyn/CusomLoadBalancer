package ihar.shyn.lb;

import ihar.shyn.model.BackendInstanceWithWeight;

public interface WeightLoadBalancer {
    boolean register(BackendInstanceWithWeight backendInstanceWithWeight);
    BackendInstanceWithWeight get();
}
