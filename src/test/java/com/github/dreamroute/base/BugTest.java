package com.github.dreamroute.base;

public class BugTest {

    public static void main(String[] args) throws InterruptedException {
        Resource resource = new Resource();
        AddRunner add = new AddRunner(resource);
        MinusRunner minus = new MinusRunner(resource);
        for (int i = 0; i < 10; i++) {
            new Thread(add).start();
            new Thread(minus).start();
        }
    }

    static class AddRunner implements Runnable {
        private Resource resource;

        public AddRunner(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (;;)
                this.resource.add();
        }
    }

    static class MinusRunner implements Runnable {
        private Resource resource;

        public MinusRunner(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (;;)
                this.resource.minus();
        }
    }

    static class Resource {
        int data = 0;

        public synchronized void add() {
            while (data == 1) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data++;
            System.err.println(data);
            this.notifyAll();
        }

        public synchronized void minus() {
            while (data == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data--;
            System.err.println(data);
            this.notifyAll();
        }
    }

}
