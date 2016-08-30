package cn.donica.slcd.dmanager.common;

import java.io.Serializable;
import java.util.Comparator;


public class ThreadPriorityQueueHelper implements Comparator<Thread>, Serializable {
    @Override
    public int compare(Thread lhs, Thread rhs) {
        int value = lhs.getPriority() < rhs.getPriority() ? 1 : lhs.getPriority() > rhs.getPriority() ? -1 : 0;
        return value;
    }
}
