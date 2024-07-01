package ihar.shyn.lb;

import ihar.shyn.model.BackendInstance;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RandomLoadBalancer implements LoadBalancer{

    private final int maxInstanceCount;
    private final Map<String, BackendInstance> storage;
    private final Random random = new Random();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public RandomLoadBalancer(int maxInstanceCount) {
        this.maxInstanceCount = maxInstanceCount;
        storage = new HashMap<>(maxInstanceCount);
    }

    @Override
    public boolean register(BackendInstance serverInstance) {
        if(serverInstance == null || serverInstance.getAddress() == null) {
            throw new RuntimeException("Server instance is incorrect");
        }
        try {
            lock.writeLock().lock();
            if (storage.size() >= maxInstanceCount) {
                return false;
            }
            storage.put(serverInstance.getAddress(), serverInstance);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public BackendInstance get() {
        try {
            lock.readLock().lock();
            List<BackendInstance> lst = new ArrayList<>(storage.values());
            int currentCount = lst.size();
            if (currentCount == 0) {
                return null;
            }
            return lst.get(random.nextInt(currentCount));
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean remove(BackendInstance backendInstance) {
        if (backendInstance == null || backendInstance.getAddress() == null) {
            return false;
        }
        try {
            lock.writeLock().lock();
            return storage.remove(backendInstance.getAddress()) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
