package ihar.shyn.service;

import ihar.shyn.model.BackendInstance;

public interface LoadBalancingService {
    boolean register(BackendInstance serverInstance);

    BackendInstance get();

    boolean remove(BackendInstance backendInstance);
}
