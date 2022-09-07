package test.polymor;

public class Master {
    private String name;

    public Master(String name) {
        this.name = name;
    }

    public void feed(Animal animal, Food food) {
        System.out.println(this.getName() + "给" + animal.getAname() + "喂" + food.getFname());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
