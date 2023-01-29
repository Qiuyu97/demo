package com.qiuyu.demo.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyReentrantLock implements Lock {
    private Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, time);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    /**
     * 继承AbstractQueuedSynchronizer,重写tryAcquire，tryRelease方法.就可以实现可重入独占锁。
     */
    public class Sync extends AbstractQueuedSynchronizer {
        /**
         * 尝试获取锁逻辑
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {

            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }
            else if (getExclusiveOwnerThread() == Thread.currentThread()) {
                setState(getState() + 1);
                return true;
            }
            return false;
        }

        /**
         * 尝试释放所
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getExclusiveOwnerThread() != Thread.currentThread()) {
                throw new RuntimeException();
            }
            int state = getState() - 1;
            //减少可重入次数
            if (state > 0) {
                setState(state);
                return false;
            }
            //释放锁
            setState(state);
            setExclusiveOwnerThread(null);
            return true;

        }

        //
        Condition newCondition() {
            return new ConditionObject();
        }

    }
}