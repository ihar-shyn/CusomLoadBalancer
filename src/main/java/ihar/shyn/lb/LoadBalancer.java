package ihar.shyn.lb;

import ihar.shyn.model.BackendInstance;

public interface LoadBalancer {

    boolean register(BackendInstance serverInstance);

    BackendInstance get();

    boolean remove(BackendInstance backendInstance);
}
