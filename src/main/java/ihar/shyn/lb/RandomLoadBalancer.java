package ihar.shyn.lb;

import ihar.shyn.model.BackendInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RandomLoadBalancer implements LoadBalancer{

    private final int maxInstanceCount;
    private final ConcurrentMap<String, BackendInstance> storage;
    private final Random random = new Random();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public RandomLoadBalancer(int maxInstanceCount) {
        this.maxInstanceCount = maxInstanceCount;
        storage = new ConcurrentHashMap<>(maxInstanceCount);
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
}
