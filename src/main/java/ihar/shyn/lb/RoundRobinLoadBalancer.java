package ihar.shyn.lb;

import ihar.shyn.model.BackendInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RoundRobinLoadBalancer implements LoadBalancer {

    private final List<BackendInstance> storage = new ArrayList<>();
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final int maxInstanceCount;

    public RoundRobinLoadBalancer(int maxInstanceCount) {
        this.maxInstanceCount = maxInstanceCount;
    }

    @Override
    public boolean register(BackendInstance serverInstance) {
        if (serverInstance == null || serverInstance.getAddress() == null) {
            throw new RuntimeException("Server instance is incorrect");
        }

        lock.writeLock().lock();
        try {
            if (storage.size() >= maxInstanceCount) {
                return false;
            }
            storage.remove(serverInstance);
            storage.add(serverInstance);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public BackendInstance get() {
        lock.readLock().lock();
        try {
            if (storage.size() == 0) {
                throw new RuntimeException("No servers available");
            }
            int index = currentIndex.getAndUpdate(i ->  (i+1) % storage.size());
            return storage.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean remove(BackendInstance backendInstance) {
        lock.writeLock().lock();
        try {
            if (!storage.contains(backendInstance)) {
                return false;
            }
            int indexOfRemoved = storage.indexOf(backendInstance);
            boolean result = storage.remove(backendInstance);

            if (result) {
                if (currentIndex.get() == storage.size()) {
                    currentIndex.set(0);
                } else if (currentIndex.get() > indexOfRemoved) {
                    currentIndex.set(currentIndex.get() - 1);
                }
            }
            return result;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
