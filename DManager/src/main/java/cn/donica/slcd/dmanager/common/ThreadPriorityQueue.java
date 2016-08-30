package cn.donica.slcd.dmanager.common;

import android.os.AsyncTask;

import java.util.PriorityQueue;


public class ThreadPriorityQueue<E extends Thread> {
    private final PriorityQueue<E> mQueue;
    private boolean queueIsRunning = false;
    private ThreadAsyncTask mTask;

    private ThreadPriorityQueue(QueueBuilder builder) {
        mQueue = builder.mQueue;
    }

    /**
     * Cancel the execution of all threads
     */
    public void removeAll() {
        if (mQueue != null && !mQueue.isEmpty()) {
            mQueue.clear();
        }
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    public void addThread(E t, int priority) {
        if (t != null) {
            int pId = (priority >= 0 && priority <= 10) ? priority : 1;
            t.setPriority(pId);
            mQueue.add(t);
        }
    }

    /**
     * @param t remove a Thread
     * @return
     */
    public boolean removeThread(Thread t) {
        return t != null && mQueue.remove(t);
    }

    public int size() {
        return mQueue != null ? mQueue.size() : 0;
    }

    private void runByOrder() {
        if (threadQueueIsRunning()) {
            return;
        }
        queueIsRunning = true;
        Thread mThread = null;
        boolean flag = false;
        int i = 0;
        int size = mQueue.size();
        while (!mQueue.isEmpty()) {
            if (!flag) {
                mThread = mQueue.poll();
                mThread.start();
                flag = true;
                i++;
            }
            //mThread.getState() == Thread.State.TERMINATED为检查子线程是否完成
            if (mThread.getState() == Thread.State.TERMINATED && !mQueue.isEmpty()) {
                if (size == i) {
                    break;
                }
                if (size == (i + 1)) {
                    //获取队列头部的元素但不删除该元素，如果队列为空，则返回null。
                    mThread = mQueue.peek();
                    mThread.start();
                    i++;
                } else {
                    //获取并删除队列头部的元素，如果队列为空，则返回null。
                    mThread = mQueue.poll();
                    mThread.start();
                    i++;
                }
            }
        }
        queueIsRunning = false;
    }

    /**
     * execute Thread by order
     */
    public void run() {
        mTask = new ThreadAsyncTask(this);
        mTask.execute();
    }

    class ThreadAsyncTask extends AsyncTask<Void, Void, Void> {
        ThreadPriorityQueue threadQueue;

        public ThreadAsyncTask(ThreadPriorityQueue queue) {
            this.threadQueue = queue;
        }

        @Override
        protected Void doInBackground(Void... params) {
            threadQueue.runByOrder();
            return null;
        }
    }

    private boolean threadQueueIsRunning() {
        return queueIsRunning;
    }

    public static class QueueBuilder {
        private final PriorityQueue mQueue;

        public QueueBuilder() {
            mQueue = new PriorityQueue<>(11, new ThreadPriorityQueueHelper());
        }

        public QueueBuilder addThread(Thread t, int priority) {
            if (t != null) {
                int pId = (priority >= 0 && priority <= 10) ? priority : 1;
                t.setPriority(pId);
                //将指定元素加入队列的尾部
                mQueue.add(t);
            }
            return this;
        }

        public ThreadPriorityQueue create() {
            return new ThreadPriorityQueue(this);
        }
    }
}
