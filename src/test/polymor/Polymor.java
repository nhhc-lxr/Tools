package test.polymor;

import java.util.ArrayList;

public class Polymor {
    public static void main(String[] args) {
        Master jack = new Master("Jack");
        Animal tom = new Cat("tom");
        Dog jerry = new Dog("jerry");
        Bone bone = new Bone("Bone");
        Fish fish = new Fish("Fish");
        jack.feed(tom, fish);
        jack.feed(jerry, bone);
        System.out.println(jerry.age);
        new ArrayList<>();
    }
}
