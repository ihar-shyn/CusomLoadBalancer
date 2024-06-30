package ihar.shyn.lb;

import ihar.shyn.instance.BackendInstance;

public interface LoadBalancer {

    boolean register(BackendInstance serverInstance);

    BackendInstance get();
}
