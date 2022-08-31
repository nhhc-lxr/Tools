package test.multithreading;

public class Thread01 {
    public static void main(String[] args) {
        Dog dog = new Dog();
        Thread thread = new Thread(dog);
        thread.setName("waibiwabi");
        thread.start();
    }
}

class Dog implements Runnable {
    int i = 0;

    public Dog() {
    }

    @Override
    public void run() {
        while (i++ < 1000) {
            System.out.println(i+"汪汪"+Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}