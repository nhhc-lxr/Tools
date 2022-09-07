package test.reflection;


import java.lang.reflect.Method;

public class Reflection01 {
    public static void main(String[] args) throws Exception {
        Class cls = Class.forName("test.reflection.Car");
        Object o = cls.getDeclaredConstructor(String.class).newInstance("劳斯莱斯");
        Method method = cls.getDeclaredMethod("printCar");
        method.invoke(o);
    }
}