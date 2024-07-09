package ihar.shyn.lb;

import ihar.shyn.model.BackendInstanceWithWeight;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TreeBasedWeightLoadBalancer implements WeightLoadBalancer{
    private final Random random = new Random();
    ConcurrentSkipListMap<Integer, BackendInstanceWithWeight> storage;
    private int totalWeight = 0;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    public TreeBasedWeightLoadBalancer() {
        storage = new ConcurrentSkipListMap<>();
    }

    @Override
    public boolean register(BackendInstanceWithWeight backendInstanceWithWeight) {
        if (backendInstanceWithWeight == null || backendInstanceWithWeight.getWeight() == 0) {
            return false;
        }

        lock.writeLock().lock();
        try {
            ConcurrentSkipListMap<Integer, BackendInstanceWithWeight> nStorage = new ConcurrentSkipListMap<>();
            int nTotalWeight = backendInstanceWithWeight.getWeight();
            nStorage.put(nTotalWeight, backendInstanceWithWeight);
            for (BackendInstanceWithWeight oldInstance : storage.values()) {
                nTotalWeight+=oldInstance.getWeight();
                nStorage.put(nTotalWeight, oldInstance);
            }
            this.storage = nStorage;
            this.totalWeight = nTotalWeight;
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public BackendInstanceWithWeight get() {
        lock.readLock().lock();
        try {
            int rnd = random.nextInt(this.totalWeight);
            return storage.ceilingEntry(rnd).getValue();
        } finally {
            lock.readLock().unlock();
        }

    }
}
