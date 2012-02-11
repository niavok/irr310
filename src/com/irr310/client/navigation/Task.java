package com.irr310.client.navigation;


public abstract class Task {

    private boolean completed = false;

    protected void complete() {
        synchronized (this) {
            completed = true;
            this.notify();
        }
    }

    public abstract void start();

    public void waitCompleted() {
        while (!completed) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void startAndWait() {
        start();
        waitCompleted();
    }

}
