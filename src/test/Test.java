package test;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        List list = new ArrayList();
        list.add(1);
        list.add(0);
        list.remove("1");
        System.out.println(list);
    }

}
