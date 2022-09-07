package test.multithreading;

public class Thread01 {
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        Thread thread = new Thread(new Dog());
        while (i++ < 10) {
            Thread.sleep(1000);
            System.out.println("hi" + (i));
            if (i == 5) {
                thread.start();
                thread.join();
            }
        }

    }
}

class Dog implements Runnable {
    int i = 0;

    public Dog() {
    }

    @Override
    public void run() {
        while (i++ < 10) {
            System.out.println(i + "hello" + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}