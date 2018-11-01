package com.github.dreamroute.base;

public class Resource {

    public int number = 0;

    public synchronized void add() {
        while (number == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        number++;
        System.err.println(number + "-" + Thread.currentThread().getId());
        notifyAll();
    }

    public synchronized void minus() {
        while (number == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        number--;
        System.err.println(number + "-" + Thread.currentThread().getId());
        notifyAll();
    }

    public static class AddThread implements Runnable {

        private Resource resource;

        public AddThread(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (;;)
                resource.add();
        }
    }

    public static class MinusThread implements Runnable {
        private Resource resource;

        public MinusThread(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (;;)
                resource.minus();
        }
    }

    public static void main(String[] args) {
        Resource resource = new Resource();
        for (int i = 0; i < 100; i++) {
            new Thread(new AddThread(resource)).start();
            new Thread(new MinusThread(resource)).start();
        }
    }
}